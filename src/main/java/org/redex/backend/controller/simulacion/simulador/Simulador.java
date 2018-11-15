package org.redex.backend.controller.simulacion.simulador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.*;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
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

        for (Paquete paquete : paquetes) {
            acciones.add(SimulacionAccionWrapper.of(paquete));
            if (paquete.getRutaGenerada()) {
                continue;
            }
            LocalDateTime fechaInicio = paquete.getFechaIngreso();
            LocalDateTime fechaFin = paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()
                    ? fechaInicio.plusHours(25L)
                    : fechaInicio.plusHours(49L);

            List<VueloAgendado> vas = gestorVuelosAgendados.allAlgoritmo(fechaInicio, fechaFin);

            Evolutivo e = new Evolutivo();
            
            try {
                List<VueloAgendado> ruta = e.run(paquete, vas, new ArrayList<>(), oficinasList);
                int cont = 0;
                for (VueloAgendado item : ruta) {
                    cont++;
                    if (cont == ruta.size()) {
                        item.setCantidadSalida(item.getCantidadSalida() + 1);
                        item.setCapacidadActual(item.getCapacidadActual() + 1);
                    } else {
                        item.setCapacidadActual(item.getCapacidadActual() + 1);
                    }
                }
                logger.info("RUTA: {} [{}=>{}]", paquete.getFechaIngreso(), paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
            } catch (PathNotFoundException pex) {
                logger.error("RUTA: {} [{}=>{}]", paquete.getFechaIngreso(), paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
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
