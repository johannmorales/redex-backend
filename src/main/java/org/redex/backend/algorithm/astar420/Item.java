package org.redex.backend.algorithm.astar420;

import java.util.List;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Item {

    private List<VueloAgendado> currentFlights;
    private Long cost;
    private Oficina header;

    public Item(List<VueloAgendado> vA) {
        this.currentFlights = vA;
        if (!vA.isEmpty()) {
            this.header = vA.get(vA.size() - 1).getOficinaDestino();
        } else {
            this.header = null;
        }
        this.cost = calcularCosto();
    }

    public VueloAgendado getLastFlight() {
        return currentFlights.get(currentFlights.size() - 1);
    }
    
    public long calcularCosto() {
        long acc = 0;
        for (VueloAgendado currentFlight : currentFlights) {
            acc += currentFlight.getPorcentajeUsado() * 420 + currentFlight.getOficinaDestino().getPorcentajeUsado() * 69;
        }

        return acc;
    }

    public List<VueloAgendado> getCurrentFlights() {
        return currentFlights;
    }

    public void setCurrentFlights(List<VueloAgendado> currentFlights) {
        this.currentFlights = currentFlights;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Oficina getHeader() {
        return header;
    }

    public void setHeader(Oficina header) {
        this.header = header;
    }

}
