package org.redex.backend.algorithm;

import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import org.redex.backend.model.simulacion.SimulacionVuelo;

public class AlgoritmoVuelo {

    private Long id;

    private AlgoritmoOficina oficinaOrigen;

    private AlgoritmoOficina oficinaDestino;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer capacidad;

    public static AlgoritmoVuelo of(SimulacionVuelo sv, Map<String, AlgoritmoOficina> oficinas){
        AlgoritmoVuelo av = new AlgoritmoVuelo();
        av.id = sv.getId();
        av.oficinaOrigen = oficinas.get(sv.getOficinaOrigen().getCodigo());
        av.oficinaDestino = oficinas.get(sv.getOficinaDestino().getCodigo());
        av.horaInicio = sv.getHoraInicio();
        av.horaFin = sv.getHoraFin();
        av.capacidad = sv.getCapacidad();
        return av;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
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
        final AlgoritmoVuelo other = (AlgoritmoVuelo) obj;
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

    public boolean terminaMismoDia() {
        return getHoraInicio().isBefore(getHoraFin());
    }

}
