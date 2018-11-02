package org.redex.backend.algorithm.gestor;

import java.time.LocalDateTime;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;

public class MovimientoVuelo implements Comparable<MovimientoVuelo> {

    public enum Tipo {
        ENTRADA, SALIDA
    }

    private AlgoritmoVueloAgendado vueloAgendado;

    private AlgoritmoOficina oficina;

    private Tipo tipo;

    private LocalDateTime momento;

    public MovimientoVuelo(Tipo tipo, AlgoritmoVueloAgendado va) {
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

    public AlgoritmoVueloAgendado getVueloAgendado() {
        return vueloAgendado;
    }

    public void setVueloAgendado(AlgoritmoVueloAgendado vueloAgendado) {
        this.vueloAgendado = vueloAgendado;
    }

    public AlgoritmoOficina getOficina() {
        return oficina;
    }

    public void setOficina(AlgoritmoOficina oficina) {
        this.oficina = oficina;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public void setMomento(LocalDateTime momento) {
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
