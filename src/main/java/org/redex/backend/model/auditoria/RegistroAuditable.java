package org.redex.backend.model.auditoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"idUserRegistro", "instanteRegistro"},
        allowGetters = true
)
public abstract class RegistroAuditable implements Serializable {

    @JsonIgnore
    @CreatedBy
    private Long idUserRegistro;

    @CreatedDate
    private Instant instanteRegistro;

    public Long getIdUserRegistro() {
        return idUserRegistro;
    }

    public void setIdUserRegistro(Long idUserRegistro) {
        this.idUserRegistro = idUserRegistro;
    }

    public Instant getInstanteRegistro() {
        return instanteRegistro;
    }

    public void setInstanteRegistro(Instant instanteRegistro) {
        this.instanteRegistro = instanteRegistro;
    }
}
