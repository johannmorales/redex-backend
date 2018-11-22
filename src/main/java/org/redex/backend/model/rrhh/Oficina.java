package org.redex.backend.model.rrhh;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.model.general.Pais;
import org.redex.backend.zelper.exception.AppException;

@Entity
@Table(name = "oficina", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo")
})
public class Oficina implements Serializable {

    private static final Logger logger = LogManager.getLogger(Oficina.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String codigo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pais", nullable = false)
    private Pais pais;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado;

    @NotNull
    @Column(nullable = false)
    private Integer capacidadActual;

    @NotNull
    @Column(nullable = false)
    private Integer capacidadMaxima;

    public Oficina() {
    }

    public Oficina(Long id) {
        this.id = id;
    }

    public double getPorcentajeUsado() {
        return getCapacidadActual() / getCapacidadMaxima() * 100;
    }

    public void llegaPaquete() {
        this.setCapacidadActual((Integer) (this.getCapacidadActual() + 1));
    }

    public void llegaVuelo(VueloAgendado vuelo) {
        this.setCapacidadActual((Integer) (this.getCapacidadActual() + vuelo.getCapacidadActual()));
    }

    public void saleVuelo(VueloAgendado vuelo) {
        this.setCapacidadActual((Integer) (this.getCapacidadActual() - vuelo.getCapacidadActual()));
    }

    public void entregaPaquete() {
        this.setCapacidadActual((Integer) (this.getCapacidadActual() - 1));
    }

    @Override
    public String toString() {
        return String.format("Oficina %s", getCodigo());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
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

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public void agregarPaquete() {
        this.capacidadActual++;
    }

    public void agregarPaquetes(Integer cant) {
        this.capacidadActual += cant;
    }

    public void checkIntegrity(LocalDateTime momento) {
        if (this.capacidadActual > this.capacidadMaxima) {
            throw new AppException(String.format("Colapso del almacen %s [%s]", codigo, momento.format(DateTimeFormatter.ISO_DATE_TIME)));
        } else {
            logger.debug("[{}] {} {}/{} ({}%) ", momento.format(DateTimeFormatter.ISO_DATE_TIME), codigo, capacidadActual, capacidadMaxima, (double) capacidadActual / (double) capacidadMaxima * 100);
        }
    }

    public void quitarPaquetes(Integer cantidad) {
        this.capacidadActual -= cantidad;
    }
}
