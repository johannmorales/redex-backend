package org.redex.backend.algorithm.astar420;

import java.util.List;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;

public class Item {

    private List<AlgoritmoVueloAgendado> currentFlights;
    private Long cost;
    private AlgoritmoOficina header;

    public Item(List<AlgoritmoVueloAgendado> vA) {
        this.currentFlights = vA;
        if (!vA.isEmpty()) {
            this.header = vA.get(vA.size() - 1).getOficinaDestino();
        } else {
            this.header = null;
        }
        this.cost = calcularCosto();
    }

    public AlgoritmoVueloAgendado getLastFlight() {
        return currentFlights.get(currentFlights.size() - 1);
    }
    
    public long calcularCosto() {
        long acc = 0;
        for (AlgoritmoVueloAgendado currentFlight : currentFlights) {
            acc += currentFlight.getPorcentajeUsado() * 420 + currentFlight.getOficinaDestino().getPorcentajeUsado() * 69;
        }

        return acc;
    }

    public List<AlgoritmoVueloAgendado> getCurrentFlights() {
        return currentFlights;
    }

    public void setCurrentFlights(List<AlgoritmoVueloAgendado> currentFlights) {
        this.currentFlights = currentFlights;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public AlgoritmoOficina getHeader() {
        return header;
    }

    public void setHeader(AlgoritmoOficina header) {
        this.header = header;
    }

}
