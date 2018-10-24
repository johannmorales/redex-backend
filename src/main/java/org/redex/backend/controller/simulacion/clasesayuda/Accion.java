package org.redex.backend.controller.simulacion.clasesayuda;

import java.time.ZonedDateTime;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Accion {

    public enum Tipo {
        VUELO_LLEGADA, VUELO_SALIDA, PAQUETE_REGISTRO, PAQUETE_SALIDA;
    }

    private Tipo tipo;

    private ZonedDateTime momento;

    private Oficina oficina;

    private VueloAgendado vueloAgendado;

    public Accion(Tipo tipo, Paquete paquete, ZonedDateTime momento) {
        this.tipo = tipo;
        this.momento = momento;

        switch (tipo) {
            case PAQUETE_REGISTRO:
                this.oficina = paquete.getOficinaOrigen();
                break;
            case PAQUETE_SALIDA:
                this.oficina = paquete.getOficinaDestino();
                break;
            default:
                throw new AssertionError();
        }
    }

    public Accion(Tipo tipo, VueloAgendado vueloAgendado) {
        this.tipo = tipo;
        this.vueloAgendado = vueloAgendado;

        switch (tipo) {
            case VUELO_LLEGADA:
                this.oficina = vueloAgendado.getOficinaDestino();
                this.momento = vueloAgendado.getFechaInicio();
                break;
            case VUELO_SALIDA:
                this.oficina = vueloAgendado.getOficinaDestino();
                this.momento = vueloAgendado.getFechaFin();
                break;
            default:
                throw new AssertionError();
        }
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Integer getCantidad() {
        switch (tipo) {
            case PAQUETE_REGISTRO:
            case PAQUETE_SALIDA:
                return 1;
            case VUELO_SALIDA:
            case VUELO_LLEGADA:
                return vueloAgendado.getCapacidadActual();
            default:
                throw new AssertionError();
        }
    }

    public ZonedDateTime getMomento() {
        return momento;
    }

    public Oficina getOficina() {
        return oficina;
    }

}
