package org.redex.backend.controller.auditoria;

import org.redex.backend.model.auditoria.Auditoria;
import org.redex.backend.model.auditoria.AuditoriaTipoEnum;
import org.redex.backend.model.seguridad.Usuario;
import org.redex.backend.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuditoriaServiceImp implements AuditoriaService {

    @Autowired
    AuditoriaRepository auditoriaRepository;

    @Override
    @Transactional
    public void auditar(AuditoriaTipoEnum tipo) {
        Auditoria aut = new Auditoria();
        aut.setTipo(tipo);
        auditoriaRepository.save(aut);
    }
}
