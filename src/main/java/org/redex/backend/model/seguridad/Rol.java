package org.redex.backend.model.seguridad;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "rol")
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Enumerated(EnumType.STRING)
    private RolEnum codigo;

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

    public RolEnum getCodigo() {
        return codigo;
    }

    public void setCodigo(RolEnum codigo) {
        this.codigo = codigo;
    }

}
