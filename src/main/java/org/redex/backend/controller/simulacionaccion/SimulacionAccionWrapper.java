package org.redex.backend.controller.simulacionaccion;

import java.time.ZoneOffset;

import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionAccion;

public class SimulacionAccionWrapper implements Comparable<SimulacionAccionWrapper> {

    private String tipo;
    
    private String oficinaLlegada;
    
    private String oficinaSalida;
    
    private Long fechaSalida;
    
    private Long fechaLlegada;
    
    private Integer cantidad;
    
    private Integer cantidadSalida;

    private VueloAgendado va;
    
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
    
    
    public static SimulacionAccionWrapper of(VueloAgendado ava){
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


    public static SimulacionAccionWrapper of(Paquete ava){
        SimulacionAccionWrapper w = new SimulacionAccionWrapper();
        w.cantidad = 1;
        w.cantidadSalida = 0;
        w.fechaSalida= ava.getFechaIngreso().toInstant(ZoneOffset.UTC).toEpochMilli();
        w.setTipo("REGISTRO");
        w.setOficinaLlegada(ava.getOficinaOrigen().getPais().getCodigoIso());
        return w;
    }


    public int tipoValue(){
        switch (tipo){
            case "SALIDA":
                return 0;
            case "REGISTRO":
                return 1;
        }
        return 0;
    }
    @Override
    public int compareTo(SimulacionAccionWrapper o) {
        if(o.fechaSalida.equals(this.fechaSalida)){
            return Integer.compare(this.tipoValue(), o.tipoValue());
        }
        return this.fechaSalida.compareTo(o.fechaSalida);
    }
}
