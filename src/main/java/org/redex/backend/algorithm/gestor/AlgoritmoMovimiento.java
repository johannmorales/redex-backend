package org.redex.backend.algorithm.gestor;

import java.time.LocalDateTime;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;

public class AlgoritmoMovimiento implements Comparable<AlgoritmoMovimiento> {

    public enum Tipo {
        VUELO_ENTRADA, VUELO_SALIDA, PAQUETES_SALIDA
    }

    private AlgoritmoVueloAgendado vueloAgendado;

    private AlgoritmoOficina oficina;

    private Tipo tipo;

    private Integer cantidad;

    private LocalDateTime momento;

    private AlgoritmoMovimiento(Tipo tipo, AlgoritmoVueloAgendado va) {
        this.tipo = tipo;
        this.vueloAgendado = va;
        switch (tipo) {
            case VUELO_ENTRADA:
                this.oficina = va.getOficinaDestino();
                this.momento = va.getFechaFin();
                this.cantidad = va.getCapacidadActual();
                break;
            case VUELO_SALIDA:
                this.oficina = va.getOficinaOrigen();
                this.momento = va.getFechaInicio();
                this.cantidad= va.getCapacidadActual();
                break;
            case PAQUETES_SALIDA:
                this.oficina = va.getOficinaDestino();
                this.momento = va.getFechaFin();
                this.cantidad = va.getCantidadSalida();
                break;
            default:
                throw new AssertionError();
        }
    }

    public static AlgoritmoMovimiento crearSalidaVuelo(AlgoritmoVueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.VUELO_SALIDA, va);
    }

    public static AlgoritmoMovimiento crearEntradaVuelo(AlgoritmoVueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.VUELO_ENTRADA, va);
    }

    public static AlgoritmoMovimiento crearSalidaPaquetes(AlgoritmoVueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.PAQUETES_SALIDA, va);
    }

    @Override
    public int compareTo(AlgoritmoMovimiento o) {
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
            case VUELO_ENTRADA:
                return vueloAgendado.getCapacidadActual();
            case VUELO_SALIDA:
                return vueloAgendado.getCapacidadActual() * -1;
            case PAQUETES_SALIDA:
                return cantidad * -1;
            default:
                throw new AssertionError();
        }
    }
}
