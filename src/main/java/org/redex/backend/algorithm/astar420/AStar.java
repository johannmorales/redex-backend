package org.redex.backend.algorithm.astar420;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class AStar implements Algoritmo {

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<VueloAgendado> run(Paquete paquete, List<VueloAgendado> vuelosAgendados, List<Vuelo> vuelos, List<Oficina> oficinas) {
        
        Integer cantidadDias = paquete.esIntercontinental() ? 2 : 1;
        
        gestorAlgoritmo = new GestorAlgoritmo(cantidadDias, paquete.getFechaRegistro(), vuelosAgendados, vuelos, oficinas);

        PriorityQueue<Item> priorityQueue = new PriorityQueue<>(Comparator.comparing(Item::getCost));
        List<VueloAgendado> vuelosAgendaods = gestorAlgoritmo.obtenerValidos(paquete.getOficinaOrigen(), paquete.getFechaRegistro());

        for (VueloAgendado vueloAgendado : vuelosAgendaods) {
            List<VueloAgendado> vAux = new ArrayList<>();
            vAux.add(vueloAgendado);
            priorityQueue.add(new Item(vAux));
        }

        while (!priorityQueue.isEmpty()) {
        
            Item itemElegido = priorityQueue.poll();

            if (itemElegido.getHeader() == paquete.getOficinaDestino()) {
                return itemElegido.getCurrentFlights();
            }

            VueloAgendado lastFlight = itemElegido.getLastFlight();

            List<VueloAgendado> vuelosPosible = gestorAlgoritmo.obtenerValidos(lastFlight.getOficinaDestino(), lastFlight.getFechaFin());

            for (VueloAgendado vueloPosible : vuelosPosible) {
                List<VueloAgendado> vAux2 = new ArrayList<>(itemElegido.getCurrentFlights());
                vAux2.add(vueloPosible);
                priorityQueue.add(new Item(vAux2));
            }
            
        }

        throw new PathNotFoundException(paquete);
    }
}
