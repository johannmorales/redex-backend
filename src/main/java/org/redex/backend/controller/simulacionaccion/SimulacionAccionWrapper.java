package org.redex.backend.controller.simulacionaccion;

import java.time.ZoneOffset;
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
    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the oficinaLlegada
     */
    public String getOficinaLlegada() {
        return oficinaLlegada;
    }

    /**
     * @param oficinaLlegada the oficinaLlegada to set
     */
    public void setOficinaLlegada(String oficinaLlegada) {
        this.oficinaLlegada = oficinaLlegada;
    }

    /**
     * @return the oficinaSalida
     */
    public String getOficinaSalida() {
        return oficinaSalida;
    }

    /**
     * @param oficinaSalida the oficinaSalida to set
     */
    public void setOficinaSalida(String oficinaSalida) {
        this.oficinaSalida = oficinaSalida;
    }

    /**
     * @return the fechaSalida
     */
    public Long getFechaSalida() {
        return fechaSalida;
    }

    /**
     * @param fechaSalida the fechaSalida to set
     */
    public void setFechaSalida(Long fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    /**
     * @return the fechaLlegada
     */
    public Long getFechaLlegada() {
        return fechaLlegada;
    }

    /**
     * @param fechaLlegada the fechaLlegada to set
     */
    public void setFechaLlegada(Long fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    /**
     * @return the cantidad
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the cantidadSalida
     */
    public Integer getCantidadSalida() {
        return cantidadSalida;
    }

    /**
     * @param cantidadSalida the cantidadSalida to set
     */
    public void setCantidadSalida(Integer cantidadSalida) {
        this.cantidadSalida = cantidadSalida;
    }
    
    
    
    public static SimulacionAccionWrapper of(SimulacionAccion a){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.setTipo(a.getTipo().name());
        w.setOficinaLlegada(a.getOficinaLlegada().getPais().getCodigoIso());
        if(a.getOficinaSalida() != null){
            w.setOficinaSalida(a.getOficinaSalida().getPais().getCodigoIso());
        }
        w.setFechaLlegada((Long) a.getFechaLlegada().toInstant(ZoneOffset.UTC).toEpochMilli());
        if(a.getFechaSalida() != null){
            w.setFechaSalida((Long) a.getFechaSalida().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        w.setCantidad(a.getCantidad());
        if (a.getCantidadSalida() != null){
            w.setCantidadSalida(a.getCantidadSalida());
        }
        
        return w;
    }
}
