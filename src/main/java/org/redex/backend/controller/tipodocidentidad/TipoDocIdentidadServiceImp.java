package org.redex.backend.controller.tipodocidentidad;

import java.util.List;
import org.redex.backend.repository.TipoDocumentoIdentidadRepository;
import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TipoDocIdentidadServiceImp implements TipoDocIdentidadService {
    
    @Autowired
    TipoDocumentoIdentidadRepository tipoDocumentoIdentidadRepository;

    @Override
    public List<TipoDocumentoIdentidad> all() {
        return tipoDocumentoIdentidadRepository.findAll();
    }
    
    
    
 }
