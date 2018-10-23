package org.redex.backend.controller.persona;

import java.util.List;
import org.redex.backend.repository.PersonaRepository;
import org.redex.model.general.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonasServiceImp implements PersonasService {
    
    @Autowired
    PersonaRepository personaRepository;

    @Override
    public List<Persona> all() {
        return personaRepository.findAll();
    }
    
}
