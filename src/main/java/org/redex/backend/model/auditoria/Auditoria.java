package org.redex.backend.model.auditoria;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "auditoria")
public class Auditoria extends RegistroAuditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AuditoriaTipoEnum tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditoriaTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(AuditoriaTipoEnum tipo) {
        this.tipo = tipo;
    }

}
