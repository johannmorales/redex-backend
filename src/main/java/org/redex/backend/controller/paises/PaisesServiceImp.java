package org.redex.backend.controller.paises;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaisesServiceImp implements PaisesService {
    
}
