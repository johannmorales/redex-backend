package org.redex.backend.controller.auditoria;

import org.redex.backend.model.auditoria.Auditoria;
import org.redex.backend.model.auditoria.AuditoriaTipoEnum;
import org.redex.backend.repository.AuditoriaRepository;
import org.redex.backend.security.DataSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class AuditoriaServiceImp implements AuditoriaService {

    @Autowired
    AuditoriaRepository auditoriaRepository;

    @Override
    @Transactional(readOnly = false)
    public void auditar(AuditoriaTipoEnum tipo, DataSession ds) {
        Auditoria aut = new Auditoria();
        aut.setTipo(tipo);
        aut.setOficina(ds.getOficina());
        auditoriaRepository.save(aut);
    }
}
