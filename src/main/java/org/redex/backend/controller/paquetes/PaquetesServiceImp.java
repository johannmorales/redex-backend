package org.redex.backend.controller.paquetes;

import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.repository.PaquetesRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaquetesServiceImp implements PaquetesService {

    @Autowired
    PaquetesRepository paquetesRepository;
    
    @Override
    public List<Paquete> list() {
        return paquetesRepository.findAll();
    }

    @Override
    public Paquete find(Long id) {
        return paquetesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete", "id", id));
    }

}
