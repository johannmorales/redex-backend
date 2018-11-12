package org.redex.backend.controller.simulacionaccion;

import java.time.ZoneOffset;

import org.redex.backend.algorithm.AlgoritmoPaquete;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;
import org.redex.backend.model.simulacion.SimulacionAccion;

/**
 * @param tipo: tipo de la accion
 * @param oficinaLlegada: codigo ISO del pais de la oficina de llegada
 * @param oficinaSalida: codigo ISO del pais de la oficina de salida
 * @param fechaSalida fecha de salida como numero de ms
 * @param fechaLlegada fecha de llegada como numero de ms
 * @param cantidad cantidad de paquetes de la accion
 * @param cantidadSalida cantidad de paquetes que salen al terminar el vuelo
 */
public class SimulacionAccionWrapper {

    private String tipo;
    
    private String oficinaLlegada;
    
    private String oficinaSalida;
    
    private Long fechaSalida;
    
    private Long fechaLlegada;
    
    private Integer cantidad;
    
    private Integer cantidadSalida;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOficinaLlegada() {
        return oficinaLlegada;
    }

    public void setOficinaLlegada(String oficinaLlegada) {
        this.oficinaLlegada = oficinaLlegada;
    }

    public String getOficinaSalida() {
        return oficinaSalida;
    }

    public void setOficinaSalida(String oficinaSalida) {
        this.oficinaSalida = oficinaSalida;
    }

    public Long getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Long fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Long getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Long fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadSalida() {
        return cantidadSalida;
    }

    public void setCantidadSalida(Integer cantidadSalida) {
        this.cantidadSalida = cantidadSalida;
    }
    
    
    public static SimulacionAccionWrapper of(AlgoritmoVueloAgendado ava){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.cantidad = ava.getCapacidadActual();
        w.cantidadSalida = ava.getCantidadSalida();
        w.fechaSalida= ava.getFechaInicio().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.fechaLlegada = ava.getFechaFin().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.setTipo("SALIDA");
        w.setOficinaLlegada(ava.getOficinaDestino().getPais().getCodigoIso());
        w.setOficinaSalida(ava.getOficinaOrigen().getPais().getCodigoIso());
        return w;
    }


    public static SimulacionAccionWrapper of(AlgoritmoPaquete ava){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.cantidad = 1;
        w.cantidadSalida = 0;
        w.fechaSalida= ava.getFechaRegistro().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.setTipo("REGISTRO");
        w.setOficinaLlegada(ava.getOficinaOrigen().getPais().getCodigoIso());
        return w;
    }

    public static SimulacionAccionWrapper of(SimulacionAccion a){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.setTipo(a.getTipo().name());
        w.setOficinaLlegada(a.getOficinaDestino().getPais().getCodigoIso());
        if(a.getOficinaOrigen() != null){
            w.setOficinaSalida(a.getOficinaOrigen().getPais().getCodigoIso());
        }
        if(a.getFechaFin() != null){
            w.setFechaLlegada((Long) a.getFechaFin().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        if(a.getFechaInicio() != null){
            w.setFechaSalida((Long) a.getFechaInicio().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        w.setCantidad(a.getCantidad());
        if (a.getCantidadSalida() != null){
            w.setCantidadSalida(a.getCantidadSalida());
        }
        return w;
    }
}
