package org.redex.backend.controller.simulacion.clasesayuda;

import java.time.ZonedDateTime;
import java.util.List;
import org.redex.model.rrhh.Oficina;

public class SimulacionInfo {

    private ZonedDateTime inicio;

    private ZonedDateTime fin;

    private List<Accion> acciones;

    private List<Oficina> oficinas;

    public ZonedDateTime getInicio() {
        return inicio;
    }

    public void setInicio(ZonedDateTime inicio) {
        this.inicio = inicio;
    }

    public ZonedDateTime getFin() {
        return fin;
    }

    public void setFin(ZonedDateTime fin) {
        this.fin = fin;
    }

    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }

    public List<Oficina> getOficinas() {
        return oficinas;
    }

    public void setOficinas(List<Oficina> oficinas) {
        this.oficinas = oficinas;
    }

}
