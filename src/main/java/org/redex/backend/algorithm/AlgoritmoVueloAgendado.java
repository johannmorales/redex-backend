package org.redex.backend.algorithm;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public class AlgoritmoVueloAgendado {

    private Long id;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Integer capacidadActual;

    private Integer capacidadMaxima;

    private AlgoritmoOficina oficinaOrigen;

    private AlgoritmoOficina oficinaDestino;

    private Integer cantidadSalida;

    public static AlgoritmoVueloAgendado of(SimulacionVueloAgendado sva, Map<String, AlgoritmoOficina> oficinas) {
        AlgoritmoVueloAgendado ava = new AlgoritmoVueloAgendado();
        ava.setId(sva.getId());
        ava.setFechaInicio(sva.getFechaInicio());
        ava.setFechaFin(sva.getFechaFin());
        ava.setCapacidadActual(sva.getCapacidadActual());
        ava.setCapacidadMaxima(sva.getCapacidadMaxima());

        return ava;
    }

    public static AlgoritmoVueloAgendado of(VueloAgendado sva, Map<String, AlgoritmoOficina> oficinas) {
        AlgoritmoVueloAgendado ava = new AlgoritmoVueloAgendado();
        ava.setId(sva.getId());
        ava.setFechaInicio(sva.getFechaInicio());
        ava.setFechaFin(sva.getFechaFin());
        ava.setCapacidadActual(sva.getCapacidadActual());
        ava.setCapacidadMaxima(sva.getCapacidadMaxima());
        ava.setOficinaOrigen(oficinas.get(sva.getOficinaOrigen().getCodigo()));
        ava.setOficinaDestino(oficinas.get(sva.getOficinaDestino().getCodigo()));
        return ava;
    }

    @Override
    public int hashCode() {
        return getId().hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AlgoritmoVueloAgendado other = (AlgoritmoVueloAgendado) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(Integer capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getCantidadSalida() {
        return cantidadSalida;
    }

    public void setCantidadSalida(Integer cantidadSalida) {
        this.cantidadSalida = cantidadSalida;
    }
    
    public AlgoritmoOficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public AlgoritmoOficina getOficinaDestino() {
        return oficinaDestino;
    }

    public int getPorcentajeUsado() {
        return this.getCapacidadActual() / this.getCapacidadMaxima() * 100;
    }

    public void setOficinaOrigen(AlgoritmoOficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public void setOficinaDestino(AlgoritmoOficina oficinaDestino) {
        this.oficinaDestino = oficinaDestino;
    }
}
