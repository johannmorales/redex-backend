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
    private SimulacionOficina oficinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_oficina_salida", nullable = false)
    private SimulacionOficina oficinaDestino;

    @Enumerated(EnumType.STRING)
    private SimulacionAccionTipoEnum tipo;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = true)
    private Integer cantidadSalida;

    public SimulacionAccion() {
    }

    public static SimulacionAccion of(SimulacionPaquete sp){
        SimulacionAccion a = new SimulacionAccion();
        a.setFechaInicio(sp.getFechaIngreso());
        a.setOficinaOrigen(sp.getOficinaOrigen());
        a.setSimulacion(sp.getSimulacion());
        a.setTipo(SimulacionAccionTipoEnum.REGISTRO);
        a.setCantidadSalida(0);
        a.setCantidad(1);
        
        return a;
    }
    
      public static SimulacionAccion of(SimulacionVueloAgendado sv){
        SimulacionAccion a = new SimulacionAccion();
        a.setFechaFin(sv.getFechaFin());
        a.setFechaInicio(sv.getFechaInicio());
        a.setOficinaOrigen(sv.getVuelo().getOficinaOrigen());
        a.setOficinaDestino(sv.getVuelo().getOficinaDestino());
        a.setSimulacion(sv.getSimulacion());
        a.setCantidad(sv.getCapacidadActual());
        a.setCantidadSalida(sv.getCantidadSalida());
        a.setTipo(SimulacionAccionTipoEnum.SALIDA);
        
        return a;
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

    public SimulacionAccionTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(SimulacionAccionTipoEnum tipo) {
        this.tipo = tipo;
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
