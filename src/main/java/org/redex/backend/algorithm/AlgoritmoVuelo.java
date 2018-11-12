package org.redex.backend.algorithm;

import java.time.LocalTime;

public class AlgoritmoVuelo {

    private AlgoritmoOficina oficinaOrigen;

    private AlgoritmoOficina oficinaDestino;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer capacidad;

    public AlgoritmoOficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(AlgoritmoOficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public AlgoritmoOficina getOficinaDestino() {
        return oficinaDestino;
    }

    public void setOficinaDestino(AlgoritmoOficina oficinaDestino) {
        this.oficinaDestino = oficinaDestino;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
