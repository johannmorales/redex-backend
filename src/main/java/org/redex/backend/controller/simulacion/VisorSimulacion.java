package org.redex.backend.controller.simulacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.*;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class VisorSimulacion {

    private static Logger logger = LogManager.getLogger(VisorSimulacion.class);

    @Autowired
    GestorVuelosAgendados gestorVuelosAgendados;

    @Autowired
    GestorPaquetes gestorPaquetes;

    Map<String, AlgoritmoOficina> oficinas;

    List<AlgoritmoOficina> oficinasList;

    VisorSimulacion() {
        this.oficinas = new HashMap<>();
        this.oficinasList = new ArrayList<>();
    }

    public void setOficinas(List<AlgoritmoOficina> oficinas) {
        this.oficinas = new HashMap<>();
        this.oficinasList = oficinas;

        for (AlgoritmoOficina ofi : this.oficinasList) {
            this.oficinas.put(ofi.getCodigo(), ofi);
        }
    }

    public Map<String, AlgoritmoOficina> getOficinas() {
        return this.oficinas;
    }

    private List<SimulacionAccionWrapper> acciones(Ventana ventana) {
        List<AlgoritmoVueloAgendado> vuelosAgendados = gestorVuelosAgendados.allPartenEnVentana(ventana);
        List<AlgoritmoPaquete> paquetes = gestorPaquetes.allEntranVentana(ventana);

        List<SimulacionAccionWrapper> acciones = new ArrayList<>();

        for (AlgoritmoVueloAgendado vuelosAgendado : vuelosAgendados) {
            acciones.add(SimulacionAccionWrapper.of(vuelosAgendado));
        }

        for (AlgoritmoPaquete paquete : paquetes) {
            acciones.add(SimulacionAccionWrapper.of(paquete));
            if(paquete.getRutaGenerada()){
                continue;
            }
            LocalDateTime fechaInicio = paquete.getFechaRegistro();
            LocalDateTime fechaFin = paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()
                    ? fechaInicio.plusHours(24L)
                    : fechaInicio.plusHours(48L);

            List<AlgoritmoVueloAgendado> vas = gestorVuelosAgendados.allAlgoritmo(fechaInicio, fechaFin);

            Evolutivo e = new Evolutivo();
            try {
                List<AlgoritmoVueloAgendado> ruta = e.run(paquete, vas, new ArrayList<>(), oficinasList);
                int cont = 0;
                for (AlgoritmoVueloAgendado item : ruta) {
                    cont++;
                    if (cont == ruta.size()) {
                        item.setCantidadSalida(item.getCantidadSalida() + 1);
                        item.setCapacidadActual(item.getCapacidadActual() + 1);
                    } else {
                        item.setCapacidadActual(item.getCapacidadActual() + 1);
                    }
                }
                logger.info("RUTA: [{}=>{}]", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());

            } catch (PathNotFoundException pex) {
                logger.error("RUTA: [{}=>{}]", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
            }
        }

        Collections.sort(acciones, Comparator.comparing(SimulacionAccionWrapper::getFechaSalida));

        return acciones;
    }

    public List<SimulacionAccionWrapper> procesarVentana(Ventana ventana) {

        this.gestorVuelosAgendados.crearVuelosAgendadosNecesarios(ventana);

        List<SimulacionAccionWrapper> acciones = this.acciones(ventana);

        this.gestorVuelosAgendados.eliminarHasta(ventana.getFin());
        this.gestorPaquetes.eliminarHasta(ventana.getFin());

        return acciones;
    }

}
