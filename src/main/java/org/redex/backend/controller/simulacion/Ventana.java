package org.redex.backend.controller.simulacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static Ventana of(LocalDateTime inicio, LocalDateTime fin){
        Ventana v = new Ventana();
        v.setInicio(inicio);
        v.setFin(fin);
        return v;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[%s => %s]", dtf.format(inicio), dtf.format(fin));
    }
}
