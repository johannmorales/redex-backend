package org.redex.backend.controller.simulacion.simulador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.algorithm.gestor.AlgoritmoMovimiento;
import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
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

    private boolean termino;

    Simulador() {
        this.oficinas = new HashMap<>();
        this.oficinasList = new ArrayList<>();
        this.termino = false;
    }

    private void procesarPaquete(Paquete paquete, int i, Long t1) {
        if (paquete.getRutaGenerada()) {
            return;
        }

        LocalDateTime fechaInicio = paquete.getFechaIngreso();
        LocalDateTime fechaFin = paquete.getFechaMaximaEntrega();
        Ventana ventanaPaquete = Ventana.of(fechaInicio, fechaFin);

        List<VueloAgendado> vuelosTodos = gestorVuelosAgendados.allPartenEnVentana(ventanaPaquete);
        List<VueloAgendado> vuelosCumplen = vuelosTodos.stream().filter(va -> va.getCapacidadActual() + 5 < va.getCapacidadMaxima()).collect(Collectors.toList());
        List<AlgoritmoMovimiento> movimientos = gestorVuelosAgendados.allMovimientoAlgoritmo(ventanaPaquete);

        Evolutivo e = new Evolutivo();

        if (i % 500 == 0) {
            Long t2 = System.currentTimeMillis();
            Long duracion = (t2 - t1);
            logger.info("Fecha actual [{}] [{}] ({} ms)", paquete.getFechaIngreso(), i, duracion);
        }

        this.simular(paquete.getFechaIngreso());

        List<VueloAgendado> ruta = e.run(paquete, vuelosCumplen, movimientos, oficinasList);

        int cont = 0;

        for (VueloAgendado item : ruta) {
            cont++;
            if (cont == ruta.size()) {
                item.setCantidadSalida(item.getCantidadSalida() + 1);
            }
            item.setCapacidadActual(item.getCapacidadActual() + 1);
        }

    }

    private List<SimulacionAccionWrapper> acciones(Ventana ventana) {

        List<SimulacionAccionWrapper> acciones = new ArrayList<>();

        List<Paquete> paquetes = gestorPaquetes.allEntranVentana(ventana);

        Long t1 = System.currentTimeMillis();
        int i = 0;

        for (Paquete paquete : paquetes) {
            try {
                procesarPaquete(paquete, i, t1);
                paquete.getOficinaOrigen().agregarPaquete();
                paquete.getOficinaOrigen().checkIntegrity(paquete.getFechaIngreso());
                acciones.add(SimulacionAccionWrapper.of(paquete));
            } catch (AvoidableException ex) {
                continue;
            } catch (DeadSimulationException dex) {
                this.termino = true;
                break;
            }
            i++;
        }

        List<VueloAgendado> vuelosAgendados = gestorVuelosAgendados.allPartenEnVentana(ventana);

        for (VueloAgendado vuelosAgendado : vuelosAgendados) {
            acciones.add(SimulacionAccionWrapper.of(vuelosAgendado));
        }

        Collections.sort(acciones, Comparator.comparing(SimulacionAccionWrapper::getFechaSalida));

        return acciones;
    }

    private void simular(LocalDateTime fechaLimite) {
        List<Movimiento> movimientos = new ArrayList<>();

        List<VueloAgendado> vasFin = gestorVuelosAgendados.allLleganEnVentana(Ventana.of(fechaActual, fechaLimite));
        for (VueloAgendado vueloAgendado : vasFin) {
            movimientos.add(Movimiento.fromFinVuelo(vueloAgendado));
            movimientos.add(Movimiento.fromSalidaPaquetes(vueloAgendado));
        }

        List<VueloAgendado> vasInicio = gestorVuelosAgendados.allPartenEnVentana(Ventana.of(fechaActual, fechaLimite));
        for (VueloAgendado vueloAgendado : vasInicio) {
            movimientos.add(Movimiento.fromInicioVuelo(vueloAgendado));
        }

        Collections.sort(movimientos);

        for (Movimiento movimiento : movimientos) {
            movimiento.process();
        }

        this.fechaActual = fechaLimite;
    }

    public List<SimulacionAccionWrapper> procesarVentana(Ventana ventana) {
        this.gestorVuelosAgendados.crearVuelosAgendadosNecesarios(ventana);
        List<SimulacionAccionWrapper> acciones = this.acciones(ventana);
        return acciones;
    }

    public List<Vuelo> getVuelos() {
        return this.gestorVuelosAgendados.getVuelos();
    }

    public void eliminar() {
        this.termino = false;
        this.gestorPaquetes.inicializar();
        this.gestorVuelosAgendados.inicializar();
        this.oficinas = new HashMap<>();
        this.oficinasList = new ArrayList<>();
    }

    public void resetear() {
        this.termino = false;
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

    public Oficina findOficina(String almacenColapso) {
        return oficinas.get(almacenColapso);
    }

    public boolean isTermino() {
        return termino;
    }
}
