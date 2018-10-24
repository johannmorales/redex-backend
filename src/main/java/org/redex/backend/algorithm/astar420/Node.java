package org.redex.backend.algorithm.astar420;

import java.time.ZonedDateTime;
import org.redex.backend.model.rrhh.Oficina;

public class Node {

    private Oficina oficina;
    private ZonedDateTime hora;

    public Node(Oficina o, ZonedDateTime h) {
        this.oficina = o;
        this.hora = h;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public ZonedDateTime getHora() {
        return hora;
    }

    public void setHora(ZonedDateTime hora) {
        this.hora = hora;
    }

}
