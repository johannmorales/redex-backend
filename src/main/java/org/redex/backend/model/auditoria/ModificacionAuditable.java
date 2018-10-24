package org.redex.backend.model.auditoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"idUserModificacion", "instanteModificacion"},
        allowGetters = true
)
public abstract class ModificacionAuditable extends RegistroAuditable implements Serializable {

    @JsonIgnore
    @LastModifiedBy
    private Long idUserModificacion;

    @LastModifiedDate
    private Instant instanteModificacion;

    public Long getIdUserModificacion() {
        return idUserModificacion;
    }

    public void setIdUserModificacion(Long idUserModificacion) {
        this.idUserModificacion = idUserModificacion;
    }

    public Instant getInstanteModificacion() {
        return instanteModificacion;
    }

    public void setInstanteModificacion(Instant instanteModificacion) {
        this.instanteModificacion = instanteModificacion;
    }

}
