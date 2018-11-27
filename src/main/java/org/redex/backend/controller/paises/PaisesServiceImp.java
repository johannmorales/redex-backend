package org.redex.backend.controller.paises;

import java.util.List;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.model.general.Pais;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public List<Pais> allByNombre(String nombre) {
        return paisesRepository.allByNombre(nombre, PageRequest.of(0, 5));
    }

    @Override
    public Page<Pais> crimsonList(CrimsonTableRequest request) {
        return paisesRepository.crimsonList(request.getSearch(), request.createPagination());
    }


    @Override
    public Pais find(Long id) {
        Pais p = paisesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pais", "id", id));
        return p;
    }

    @Override
    @Transactional
    public void save(Pais p) {
        paisesRepository.save(p);
    }

}
