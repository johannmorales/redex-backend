package org.redex.backend.controller.johana;

import org.redex.backend.repository.OficinasRepository;
import org.redex.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JohanaServiceImp implements JohanaService {

    @Autowired
    OficinasRepository oficinasRepository;
    
    @Override
    @Transactional
    public void realizarAccion(ExamplePayload example) {
        Oficina oficina = new Oficina();
        oficina.setCodigo("232312");
        oficinasRepository.save(oficina);
    }
    
    
}
