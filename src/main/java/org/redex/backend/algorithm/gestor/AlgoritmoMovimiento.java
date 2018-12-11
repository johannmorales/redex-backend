package org.redex.backend.algorithm.gestor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import pe.albatross.zelpers.miscelanea.ObjectUtil;

public class AlgoritmoMovimiento implements Comparable<AlgoritmoMovimiento> {

    public enum Tipo {
        VUELO_ENTRADA, VUELO_SALIDA, PAQUETES_SALIDA
    }

    private VueloAgendado vueloAgendado;

    private Oficina oficina;

    private Tipo tipo;

    private LocalDateTime momento;

    private AlgoritmoMovimiento(Tipo tipo, VueloAgendado va) {
        this.tipo = tipo;
        this.vueloAgendado = va;
        switch (tipo) {
            case VUELO_SALIDA:
                this.oficina = va.getOficinaOrigen();
                this.momento = va.getFechaInicio();
                break;
            case PAQUETES_SALIDA:
                this.oficina = va.getOficinaDestino();
                this.momento = va.getFechaFin();
                break;
            case VUELO_ENTRADA:
                this.oficina = va.getOficinaDestino();
                this.momento = va.getFechaFin();
                break;
            default:
                throw new AssertionError();
        }
    }

    public static AlgoritmoMovimiento crearSalidaVuelo(VueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.VUELO_SALIDA, va);
    }

    public static AlgoritmoMovimiento crearEntradaVuelo(VueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.VUELO_ENTRADA, va);
    }

    public static AlgoritmoMovimiento crearSalidaPaquetes(VueloAgendado va){
        return new AlgoritmoMovimiento(Tipo.PAQUETES_SALIDA, va);
    }

    @Override
    public int compareTo(AlgoritmoMovimiento o) {
        if(o.getMomento().equals(momento) ){
            return Integer.compare(this.orden(), o.orden());
        }

        return momento.compareTo(o.getMomento());
    }

    public int orden(){
        switch (tipo){
            case PAQUETES_SALIDA:
            case VUELO_SALIDA:
                return 1;
            case VUELO_ENTRADA:
                return 0;
        }
        return 0;
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
                return vueloAgendado.getCantidadSalida() * -1;
            default:
                throw new AssertionError();
        }
    }

    public void process(){
        oficina.setCapacidadActual(oficina.getCapacidadActual() + this.getVariacion());
        oficina.checkIntegrity(momento, this);
    }

    @Override
    public String toString() {
        return String.format("[[ %s ]] ==> [%s] ::: %s (%d)", this.momento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), this.oficina.getCodigo(), this.tipo.name(), this.getVariacion());
    }
}
