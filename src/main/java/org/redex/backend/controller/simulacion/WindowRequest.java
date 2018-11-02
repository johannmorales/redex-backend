package org.redex.backend.controller.simulacion;

import java.time.LocalDateTime;

public class WindowRequest {

    private LocalDateTime inicio;

    private LocalDateTime fin;

    private Long simulacion;

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

    public Long getSimulacion() {
        return simulacion;
    }

    public void setSimulacion(Long simulacion) {
        this.simulacion = simulacion;
    }

}
