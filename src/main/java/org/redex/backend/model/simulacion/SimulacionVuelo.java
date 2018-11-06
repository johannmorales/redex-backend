package org.redex.backend.model.simulacion;

import java.io.Serializable;
import java.time.LocalTime;
import javax.persistence.*;

@Entity
@Table(name = "simulacion_vuelo")
public class SimulacionVuelo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private Integer capacidad;
    
    @ManyToOne
    @JoinColumn(name = "id_oficina_origen", nullable = false)
    private SimulacionOficina oficinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_oficina_destino", nullable = false)
    private SimulacionOficina oficinaDestino;

    public boolean esDeUnDia(){
        if(horaInicio.isBefore(horaFin)){
            return true;
        } else {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Simulacion getSimulacion() {
        return simulacion;
    }

    public void setSimulacion(Simulacion simulacion) {
        this.simulacion = simulacion;
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

    public SimulacionOficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(SimulacionOficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public SimulacionOficina getOficinaDestino() {
        return oficinaDestino;
    }

    public void setOficinaDestino(SimulacionOficina oficinaDestino) {
        this.oficinaDestino = oficinaDestino;
    }
    
}
