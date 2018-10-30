package org.redex.backend.controller.continentes;

import java.util.List;
import org.redex.backend.model.general.Continente;
import org.redex.backend.repository.ContinentesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContinentesServiceImp implements ContinentesService{

    @Autowired
    ContinentesRepository continentesRepository;
    
    @Override
    public List<Continente> all() {
        return continentesRepository.findAll();
    }
    
}
