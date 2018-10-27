package org.redex.backend.model.simulacion;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simulacion_paquete")
public class SimulacionPaquete implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @ManyToOne
    @JoinColumn(name = "id_oficina_origen", nullable = false)
    private SimulacionOficina oficinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_oficina_destino", nullable = false)
    private SimulacionOficina oficinaDestino;

    @Column(nullable = false)
    private ZonedDateTime fechaIngreso;

    @Column
    private ZonedDateTime fechaSalida;

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

    public ZonedDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(ZonedDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public ZonedDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(ZonedDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

}
