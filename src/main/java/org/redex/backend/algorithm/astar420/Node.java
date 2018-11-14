package org.redex.backend.algorithm.astar420;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.redex.backend.model.rrhh.Oficina;

public class Node {

    private Oficina oficina;
    private LocalDateTime hora;

    public Node(Oficina o, LocalDateTime h) {
        this.oficina = o;
        this.hora = h;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

}
