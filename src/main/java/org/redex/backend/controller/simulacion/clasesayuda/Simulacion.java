package org.redex.backend.controller.simulacion.clasesayuda;

import java.util.List;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Simulacion {

    Gestor gestor;

    public Simulacion(List<Oficina> oficinas, List<VueloAgendado> vuelosAgendados, List<Vuelo> vuelos) {
        gestor = new Gestor(oficinas, vuelosAgendados, vuelos);
    }

    public SimulacionInfo ejecutar(List<Paquete> paquetes, Algoritmo algoritmo) {
        SimulacionInfo info = new SimulacionInfo();

        for (Paquete paquete : paquetes) {
            gestor.avanzar(paquete.getFechaRegistro());
            List<VueloAgendado> vueloAgendadosActuales = gestor.obtenerVuelosAgendados();
            try {
                List<VueloAgendado> ruta = algoritmo.run(paquete, vueloAgendadosActuales, gestor.getVuelos(), gestor.getOficinas());
                gestor.commit(paquete, ruta);
            } catch (PathNotFoundException pex) {
            }
        }

        return info;
    }
}
