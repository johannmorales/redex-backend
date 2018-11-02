package org.redex.backend.algorithm.astar420;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoPaquete;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public class AStar implements Algoritmo {

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<AlgoritmoVueloAgendado> run(AlgoritmoPaquete paquete, List<AlgoritmoVueloAgendado> vuelosAgendados, List<AlgoritmoOficina> oficinas) {

        gestorAlgoritmo = new GestorAlgoritmo(vuelosAgendados, oficinas);

        PriorityQueue<Item> priorityQueue = new PriorityQueue<>(Comparator.comparing(Item::getCost));
        List<AlgoritmoVueloAgendado> vuelosAgendaods = gestorAlgoritmo.obtenerValidos(paquete.getOficinaOrigen(), paquete.getFechaRegistro());

        for (AlgoritmoVueloAgendado vueloAgendado : vuelosAgendaods) {
            List<AlgoritmoVueloAgendado> vAux = new ArrayList<>();
            vAux.add(vueloAgendado);
            priorityQueue.add(new Item(vAux));
        }

        while (!priorityQueue.isEmpty()) {

            Item itemElegido = priorityQueue.poll();

            if (itemElegido.getHeader() == paquete.getOficinaDestino()) {
                return itemElegido.getCurrentFlights();
            }

            AlgoritmoVueloAgendado lastFlight = itemElegido.getLastFlight();

            List<AlgoritmoVueloAgendado> vuelosPosible = gestorAlgoritmo.obtenerValidos(lastFlight.getOficinaDestino(), lastFlight.getFechaFin());

            for (AlgoritmoVueloAgendado vueloPosible : vuelosPosible) {
                List<AlgoritmoVueloAgendado> vAux2 = new ArrayList<>(itemElegido.getCurrentFlights());
                vAux2.add(vueloPosible);
                priorityQueue.add(new Item(vAux2));
            }

        }

        throw new PathNotFoundException(paquete);
    }

    @Override
    public List<SimulacionVueloAgendado> runBatch(List<SimulacionPaquete> paquetes, List<VueloAgendado> vuelosAgendados, List<SimulacionOficina> oficinas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
