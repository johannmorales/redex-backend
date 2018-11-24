package org.redex.backend.model.auditoria;

import org.redex.backend.model.seguridad.Usuario;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
