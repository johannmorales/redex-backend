package org.redex.backend.controller.simulacion.simulador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.algorithm.gestor.AlgoritmoMovimiento;
import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.albatross.zelpers.miscelanea.Assert;

import java.time.LocalDateTime;
import java.util.*;

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

    public List<SimulacionAccionWrapper> procesarVentana(Ventana ventana) {
        this.gestorVuelosAgendados.crearVuelosAgendadosNecesarios(ventana);
        List<SimulacionAccionWrapper> acciones = this.acciones(ventana);
        return acciones;
    }

    private List<SimulacionAccionWrapper> acciones(Ventana ventana) {
        List<VueloAgendado> vuelosAgendados = gestorVuelosAgendados.allPartenEnVentana(ventana);

        List<SimulacionAccionWrapper> acciones = new ArrayList<>();

        List<Paquete> paquetes = gestorPaquetes.allEntranVentana(ventana);
        this.gestorPaquetes.limpiarHasta(ventana.getFin());

        Integer paquetesProcesados = 0;
        Integer paquetesTotales = 0;

        for (Paquete paquete : paquetes) {
            try {
                paquetesTotales++;
                procesarPaquete(paquete);
                paquete.getOficinaOrigen().agregarPaquete();
                paquete.getOficinaOrigen().checkIntegrity(paquete.getFechaIngreso());
                acciones.add(SimulacionAccionWrapper.of(paquete));
                paquetesProcesados++;
            } catch (AvoidableException ex) {
                continue;
            } catch (DeadSimulationException dex) {
                this.termino = true;
                break;
            }
        }


        logger.info("Paquetes procesados: {}", paquetesProcesados);
        logger.info("Paquetes totales: {}", paquetesTotales);

        for (VueloAgendado vuelosAgendado : vuelosAgendados) {
            SimulacionAccionWrapper saw = SimulacionAccionWrapper.of(vuelosAgendado);
            acciones.add(saw);
        }

        Collections.sort(acciones, Comparator.comparing(SimulacionAccionWrapper::getFechaSalida));

        return acciones;
    }

    private void procesarPaquete(Paquete paquete) throws AvoidableException, DeadSimulationException {
        if(fechaActual == null || paquete.getFechaIngreso().isAfter(fechaActual)){
            simular(paquete.getFechaIngreso());
        }

        LocalDateTime fechaInicio = paquete.getFechaIngreso();
        LocalDateTime fechaFin = paquete.getFechaMaximaEntrega();

        Ventana ventanaPaquete = Ventana.of(fechaInicio, fechaFin);

        List<VueloAgendado> vuelosCumplen = gestorVuelosAgendados.allAlgoritmo(ventanaPaquete);
        List<AlgoritmoMovimiento> movimientos = gestorVuelosAgendados.allMovimientoAlgoritmo(ventanaPaquete);

        Evolutivo e = new Evolutivo();

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

    private List<VueloAgendado> simular(LocalDateTime fechaLimite) {
        List<Movimiento> movimientos = new ArrayList<>();

        List<VueloAgendado> vasFin = gestorVuelosAgendados.allLleganEnVentana(Ventana.of(fechaActual, fechaLimite));
        for (VueloAgendado vueloAgendado : vasFin) {
            if (vueloAgendado.getCapacidadActual() > 0) {
                movimientos.add(Movimiento.fromFinVuelo(vueloAgendado));
            }
            if (vueloAgendado.getCantidadSalida() > 0) {
                movimientos.add(Movimiento.fromSalidaPaquetes(vueloAgendado));
            }
        }

        List<VueloAgendado> vasInicio = gestorVuelosAgendados.allPartenEnVentana(Ventana.of(fechaActual, fechaLimite));
        for (VueloAgendado vueloAgendado : vasInicio) {
            if (vueloAgendado.getCapacidadActual() > 0) {
                movimientos.add(Movimiento.fromInicioVuelo(vueloAgendado));
            }
        }

        Collections.sort(movimientos);

        for (Movimiento movimiento : movimientos) {
//            logger.info("");
//            logger.info("PROCENSADO {} {} {}", movimiento, movimiento.vueloAgendado);
//            logger.info("\tANTES: {} ", movimiento.oficina);
            movimiento.process();
//            logger.info("\tDSPSS: {} ", movimiento.oficina);

        }


        gestorVuelosAgendados.limpiarHasta(fechaLimite);

        for (Oficina oficina : oficinasList) {
            Assert.isTrue(oficina.getCapacidadMaxima() >= oficina.getCapacidadActual(), "Capacidad actual mayor que maxima");
            Assert.isTrue(oficina.getCapacidadActual() >= 0, "Capacidad actual menor que 0");
        }

        this.fechaActual = fechaLimite;

        return vasInicio;
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

    public List<Oficina> getOficinasList() {
        return this.oficinasList;
    }

    public Oficina findOficina(String almacenColapso) {
        return oficinas.get(almacenColapso);
    }

    public boolean isTermino() {
        return termino;
    }
}
