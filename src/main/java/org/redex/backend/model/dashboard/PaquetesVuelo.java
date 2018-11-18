package org.redex.backend.model.dashboard;

import java.io.Serializable;
import org.redex.backend.model.envios.Vuelo;


public class PaquetesVuelo implements Serializable {

    private Vuelo vuelo;

    private Long cantidad;

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public PaquetesVuelo(Vuelo vuelo, Long cantidad) {
        this.vuelo = vuelo;
        this.cantidad = cantidad;
    }
    
}
