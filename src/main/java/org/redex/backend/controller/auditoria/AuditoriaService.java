package org.redex.backend.controller.auditoria;

import org.redex.backend.model.auditoria.AuditoriaTipoEnum;

public interface AuditoriaService {

    void auditar(AuditoriaTipoEnum tipo);
}
