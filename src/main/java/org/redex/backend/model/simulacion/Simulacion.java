package org.redex.backend.model.simulacion;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.redex.backend.model.auditoria.RegistroAuditable;

@Entity
@Table(name = "simulacion")
public class Simulacion extends RegistroAuditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SimulacionEstadoEnum estado;

    @Column(nullable = false)
    private Integer cantidadPaquetes;

    @Column(nullable = false)
    private Integer cantidadPaquetesEntregados;

    @Column(nullable = false)
    private Integer cantidadOficinas;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Duration duracion;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "simulacion")
    private Set<SimulacionOficina> oficinas;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "simulacion")
    private Set<SimulacionPaquete> paquetes;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "simulacion")
    private Set<SimulacionVuelo> vuelos;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "simulacion")
    private Set<SimulacionVuelo> vuelosAgendados;

    public Simulacion() {

    }

    public Simulacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimulacionEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(SimulacionEstadoEnum estado) {
        this.estado = estado;
    }

    public Integer getCantidadPaquetes() {
        return cantidadPaquetes;
    }

    public void setCantidadPaquetes(Integer cantidadPaquetes) {
        this.cantidadPaquetes = cantidadPaquetes;
    }

    public Integer getCantidadPaquetesEntregados() {
        return cantidadPaquetesEntregados;
    }

    public void setCantidadPaquetesEntregados(Integer cantidadPaquetesEntregados) {
        this.cantidadPaquetesEntregados = cantidadPaquetesEntregados;
    }

    public Integer getCantidadOficinas() {
        return cantidadOficinas;
    }

    public void setCantidadOficinas(Integer cantidadOficinas) {
        this.cantidadOficinas = cantidadOficinas;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }

    public Set<SimulacionOficina> getOficinas() {
        return oficinas;
    }

    public void setOficinas(Set<SimulacionOficina> oficinas) {
        this.oficinas = oficinas;
    }

    public Set<SimulacionPaquete> getPaquetes() {
        return paquetes;
    }

    public void setPaquetes(Set<SimulacionPaquete> paquetes) {
        this.paquetes = paquetes;
    }

    public Set<SimulacionVuelo> getVuelos() {
        return vuelos;
    }

    public void setVuelos(Set<SimulacionVuelo> vuelos) {
        this.vuelos = vuelos;
    }

    public Set<SimulacionVuelo> getVuelosAgendados() {
        return vuelosAgendados;
    }

    public void setVuelosAgendados(Set<SimulacionVuelo> vuelosAgendados) {
        this.vuelosAgendados = vuelosAgendados;
    }

}
