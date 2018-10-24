package org.redex.backend.algorithm.evolutivo;

import java.util.LinkedList;
import java.util.List;

public class Cromosoma {

    private double costo;

    private LinkedList<Gen> genes;

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public List<Gen> getGenes() {
        return genes;
    }

    public void setGenes(LinkedList<Gen> genes) {
        this.genes = genes;
    }

    public Cromosoma copy() {
        Cromosoma cromosome = new Cromosoma();
        cromosome.setGenes(new LinkedList<>(genes));
        return cromosome;
    }

}
