package org.redex.backend.model.simulacion;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.redex.backend.model.general.Pais;

@Entity
@Table(name = "simulacion_oficina")
public class SimulacionOficina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @Column(nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_pais", nullable = false)
    private Pais pais;

    @Column(nullable = false)
    private Integer capacidadInicial;

    @Column(nullable = false)
    private Integer capacidadMaxima;

    @Column(nullable = false)
    private Integer zonaHoraria;

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

    public Integer getCapacidadInicial() {
        return capacidadInicial;
    }

    public void setCapacidadInicial(Integer capacidadInicial) {
        this.capacidadInicial = capacidadInicial;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(Integer zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

}
