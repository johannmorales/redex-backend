package org.redex.backend.model.envios;

import java.io.Serializable;
import java.util.List;
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
import org.redex.backend.model.general.EstadoEnum;

@Entity
@Table(name = "plan_vuelo")
public class PlanVuelo extends RegistroAuditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;
    
    @OneToMany(mappedBy = "planVuelo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vuelo> vuelos;

    public PlanVuelo() {
    }

    public PlanVuelo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void setVuelos(List<Vuelo> vuelos) {
        this.vuelos = vuelos;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

}
