package org.redex.backend.model.auditoria;

import org.hibernate.annotations.Immutable;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.model.seguridad.Usuario;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "v_auditoria_view")
public class AuditoriaView implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditoriaTipoEnum tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oficina")
    private Oficina oficina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario               ")
    private Usuario usuario;

    @Column(name = "momento")
    private LocalDateTime momento;

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

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public void setMomento(LocalDateTime momento) {
        this.momento = momento;
    }
}
