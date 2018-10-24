package org.redex.backend.model.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pais", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nombre")
    ,
    @UniqueConstraint(columnNames = "codigo")
})
public class Pais implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonInclude
    @JoinColumn(name = "id_continente")
    private Continente continente;

    @Column
    private String nombre;

    @Column
    private String codigo;

    public Pais() {
    }

    public Pais(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Continente getContinente() {
        return continente;
    }

    public void setContinente(Continente continente) {
        this.continente = continente;
    }

}
