package org.redex.backend.model.general;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "documento_identidad")
public class TipoDocumentoIdentidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais pais;

    @Column
    private String nombre;

    @Column
    private String simbolo;

    public TipoDocumentoIdentidad() {
    }

    public TipoDocumentoIdentidad(Long id) {
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

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

}
