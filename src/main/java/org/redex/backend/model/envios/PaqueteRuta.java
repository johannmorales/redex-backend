package org.redex.backend.model.envios;

import java.io.Serializable;
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
@Table(name = "paquete_ruta")
public class PaqueteRuta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_paquete", nullable = false)
    private Paquete paquete;

    @ManyToOne
    @JoinColumn(name = "id_vuelo_agendado", nullable = false)
    private VueloAgendado vueloAgendado;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RutaEstadoEnum estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public VueloAgendado getVueloAgendado() {
        return vueloAgendado;
    }

    public void setVueloAgendado(VueloAgendado vueloAgendado) {
        this.vueloAgendado = vueloAgendado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public RutaEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(RutaEstadoEnum estado) {
        this.estado = estado;
    }

}
