package org.redex.backend.algorithm.gestor;

import java.time.ZonedDateTime;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class MovimientoVuelo implements Comparable<MovimientoVuelo> {

    public enum Tipo {
        ENTRADA, SALIDA
    }

    private VueloAgendado vueloAgendado;

    private Oficina oficina;

    private Tipo tipo;

    private ZonedDateTime momento;

    public MovimientoVuelo(Tipo tipo, VueloAgendado va) {
        this.tipo = tipo;
        this.vueloAgendado = va;
        switch (tipo) {
            case ENTRADA:
                this.oficina = va.getOficinaDestino();
                this.momento = va.getFechaFin();
                break;
            case SALIDA:
                this.oficina = va.getOficinaOrigen();
                this.momento = va.getFechaInicio();
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public int compareTo(MovimientoVuelo o) {
        return momento.compareTo(o.getMomento());
    }

    public VueloAgendado getVueloAgendado() {
        return vueloAgendado;
    }

    public void setVueloAgendado(VueloAgendado vueloAgendado) {
        this.vueloAgendado = vueloAgendado;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getMomento() {
        return momento;
    }

    public void setMomento(ZonedDateTime momento) {
        this.momento = momento;
    }

    public Integer getVariacion() {
        switch (tipo) {
            case ENTRADA:
                return vueloAgendado.getCapacidadActual();
            case SALIDA:
                return vueloAgendado.getCapacidadActual() * -1;
            default:
                throw new AssertionError();
        }
    }
}
