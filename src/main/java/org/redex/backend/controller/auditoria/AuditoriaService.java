package org.redex.backend.controller.auditoria;

import org.redex.backend.model.auditoria.AuditoriaTipoEnum;
import org.redex.backend.security.DataSession;

public interface AuditoriaService {

    void auditar(AuditoriaTipoEnum tipo, DataSession ds);
}
