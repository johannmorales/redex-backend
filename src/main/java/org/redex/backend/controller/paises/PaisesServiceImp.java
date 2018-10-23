package org.redex.backend.controller.paises;

import java.util.List;
import org.redex.backend.repository.PaisesRepository;
import org.redex.model.general.Pais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaisesServiceImp implements PaisesService {

    @Autowired
    PaisesRepository paisesRepository;
    
    @Override
    public List<Pais> all() {
        return paisesRepository.findAll();
    }
    
}
