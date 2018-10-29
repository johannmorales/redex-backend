package org.redex.backend.model.simulacion;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simulacion_accion")
public class SimulacionAccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @ManyToOne
    @JoinColumn(name = "id_oficina_llegada", nullable = false)
    private SimulacionOficina oficinaLlegada;

    @ManyToOne
    @JoinColumn(name = "id_oficina_salida", nullable = false)
    private SimulacionOficina oficinaSalida;

    @Enumerated(EnumType.STRING)
    private SimulacionAccionTipoEnum tipo;

    @Column(nullable = false)
    private LocalDateTime fechaSalida;

    @Column(nullable = false)
    private LocalDateTime fechaLlegada;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = true)
    private Integer cantidadSalida;

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

    public SimulacionAccionTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(SimulacionAccionTipoEnum tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
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

    public SimulacionOficina getOficinaLlegada() {
        return oficinaLlegada;
    }

    public void setOficinaLlegada(SimulacionOficina oficinaLlegada) {
        this.oficinaLlegada = oficinaLlegada;
    }

    public SimulacionOficina getOficinaSalida() {
        return oficinaSalida;
    }

    public void setOficinaSalida(SimulacionOficina oficinaSalida) {
        this.oficinaSalida = oficinaSalida;
    }

}
