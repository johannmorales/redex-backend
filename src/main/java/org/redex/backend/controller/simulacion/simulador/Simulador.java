package org.redex.backend.controller.simulacion.simulador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

@Component
public class Simulador {

    private static Logger logger = LogManager.getLogger(Simulador.class);

    @Autowired
    GestorVuelosAgendados gestorVuelosAgendados;

    @Autowired
    GestorPaquetes gestorPaquetes;

    private Map<String, Oficina> oficinas;

    private List<Oficina> oficinasList;

    private LocalDateTime fechaActual;

    Simulador() {
        this.oficinas = new HashMap<>();
        this.oficinasList = new ArrayList<>();
    }

    private List<SimulacionAccionWrapper> acciones(Ventana ventana) {
        List<VueloAgendado> vuelosAgendados = gestorVuelosAgendados.allPartenEnVentana(ventana);
        List<Paquete> paquetes = gestorPaquetes.allEntranVentana(ventana);

        List<SimulacionAccionWrapper> acciones = new ArrayList<>();

        for (VueloAgendado vuelosAgendado : vuelosAgendados) {
            acciones.add(SimulacionAccionWrapper.of(vuelosAgendado));
        }


        Integer i = 0;
        Long t1 = System.currentTimeMillis();

        for (Paquete paquete : paquetes) {
            acciones.add(SimulacionAccionWrapper.of(paquete));

            if (paquete.getRutaGenerada()) {
                continue;
            }

            LocalDateTime fechaInicio = paquete.getFechaIngreso();
            LocalDateTime fechaFin = paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()
                    ? fechaInicio.plusHours(24L)
                    : fechaInicio.plusHours(48L);

            Ventana ventanaPaquete = Ventana.of(fechaInicio, fechaFin);

            List<VueloAgendado> vuelosTodos = gestorVuelosAgendados.allAlgoritmo(fechaInicio, fechaFin);
            List<VueloAgendado> vuelosCumplen = vuelosTodos.stream().filter(va -> va.getCapacidadActual() < va.getCapacidadMaxima()).collect(Collectors.toList());
            List<VueloAgendado> vuelosParten = gestorVuelosAgendados.allPartenEnVentana(ventanaPaquete);
            List<VueloAgendado> vuelosLlegan = gestorVuelosAgendados.allLleganEnVentana(ventanaPaquete);

            Evolutivo e = new Evolutivo();

            try {

                if (i % 10 == 0) {
                    Long t2 = System.currentTimeMillis();
                    Long duracion = t2 - t1;
                    logger.info("Fecha actual [{}] [{}] ({} ms)", paquete.getFechaIngreso(), i, duracion);
                    t1 = t2;
                }

                i++;

                paquete.getOficinaOrigen().agregarPaquete();
                paquete.getOficinaOrigen().checkIntegrity(paquete.getFechaIngreso());

                this.simular(paquete.getFechaIngreso());
                Long t3 = System.currentTimeMillis();
                List<VueloAgendado> ruta = e.run(paquete, vuelosTodos, vuelosCumplen, vuelosParten, vuelosLlegan, oficinasList);
                Long t4 = System.currentTimeMillis();

                // logger.info("Algoritmo corrio en {} ms\n", t4-t3);

                int cont = 0;
                for (VueloAgendado item : ruta) {
                    cont++;
                    if (cont == ruta.size()) {
                        item.setCantidadSalida(item.getCantidadSalida() + 1);
                    }

                    item.setCapacidadActual(item.getCapacidadActual() + 1);

                }


                //logger.info("RUTA: {} [{}=>{}]", paquete.getFechaIngreso(), paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
            } catch (AvoidableException pex) {
                //pex.printStackTrace();
                //logger.error("RUTA: {} [{}=>{}] NO HAY VUELO POSIBLE", paquete.getFechaIngreso(), paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
            }
        }

        Collections.sort(acciones, Comparator.comparing(SimulacionAccionWrapper::getFechaSalida));

        return acciones;
    }

    private void simular(LocalDateTime fechaIngreso) {

        List<Movimiento> movimientos = new ArrayList<>();

        List<VueloAgendado> vasFin = gestorVuelosAgendados.allLleganEnVentana(Ventana.of(fechaActual, fechaIngreso));
        for (VueloAgendado vueloAgendado : vasFin) {
            movimientos.add(Movimiento.fromFinVuelo(vueloAgendado));
        }

        List<VueloAgendado> vasInicio = gestorVuelosAgendados.allPartenEnVentana(Ventana.of(fechaActual, fechaIngreso));
        for (VueloAgendado vueloAgendado : vasInicio) {
            movimientos.add(Movimiento.fromInicioVuelo(vueloAgendado));
        }

        Collections.sort(movimientos);

        for (Movimiento movimiento : movimientos) {
            if (movimiento.oficina.getCodigo().equals("SPIM")) {
                //   movimiento.log();
            }
            movimiento.process();
        }

        this.fechaActual = fechaIngreso;

    }

    public List<SimulacionAccionWrapper> procesarVentana(Ventana ventana) {

        this.gestorVuelosAgendados.crearVuelosAgendadosNecesarios(ventana);

        List<SimulacionAccionWrapper> acciones = this.acciones(ventana);

        this.gestorVuelosAgendados.eliminarHasta(ventana.getFin());
        this.gestorPaquetes.eliminarHasta(ventana.getFin());

        return acciones;
    }

    public List<Vuelo> getVuelos() {
        return this.gestorVuelosAgendados.getVuelos();
    }

    public void eliminar() {
        this.gestorPaquetes.inicializar();
        this.gestorVuelosAgendados.inicializar();
        this.oficinas = new HashMap<>();
        this.oficinasList = new ArrayList<>();
    }

    public void resetear() {
        this.gestorVuelosAgendados.reiniciar();
        this.gestorPaquetes.inicializar();
    }

    public void setVuelos(List<Vuelo> vuelos) {
        this.gestorVuelosAgendados.setVuelos(vuelos);
        logger.info("{} vuelos agregados", vuelos.size());
    }

    public void setOficinas(List<Oficina> oficinas) {
        this.oficinas = new HashMap<>();
        this.oficinasList = oficinas;

        for (Oficina ofi : this.oficinasList) {
            this.oficinas.put(ofi.getCodigo(), ofi);
        }

        logger.info("{} oficinas agregadas", this.oficinas.size());
    }

    public Map<String, Oficina> getOficinas() {
        return this.oficinas;
    }

}
