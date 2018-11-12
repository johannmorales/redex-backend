package org.redex.backend.controller.simulacion;

import java.time.LocalDateTime;

public class Ventana {
    private LocalDateTime inicio;

    private LocalDateTime fin;

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
