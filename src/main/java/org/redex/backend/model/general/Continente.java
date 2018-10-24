package org.redex.backend.model.general;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "continente")
public class Continente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    public Continente() {
    }

    public Continente(Long id) {
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

}
