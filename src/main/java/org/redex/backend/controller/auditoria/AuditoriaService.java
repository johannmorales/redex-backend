package org.redex.backend.controller.auditoria;

import org.redex.backend.model.auditoria.AuditoriaTipoEnum;
import org.redex.backend.model.seguridad.Usuario;

public interface AuditoriaService {

    void auditar(Usuario usuario, AuditoriaTipoEnum tipo);
}
