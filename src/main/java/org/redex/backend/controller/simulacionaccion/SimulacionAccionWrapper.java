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
    
    public String tipo;
    
    public String oficinaLlegada;
    
    public String oficinaSalida;
    
    public Long fechaSalida;
    
    public Long fechaLlegada;
    
    public Integer cantidad;
    
    public Integer cantidadSalida;
    
    public static SimulacionAccionWrapper of(SimulacionAccion a){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.tipo = a.getTipo().name();
        w.oficinaLlegada = a.getOficinaLlegada().getPais().getCodigoIso();
        w.oficinaSalida = a.getOficinaSalida().getPais().getCodigoIso();
        w.fechaLlegada =a.getFechaLlegada().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.fechaSalida = a.getFechaSalida().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.cantidad = a.getCantidad();
        w.cantidadSalida = a.getCantidadSalida();
        
        return w;
    }
}
