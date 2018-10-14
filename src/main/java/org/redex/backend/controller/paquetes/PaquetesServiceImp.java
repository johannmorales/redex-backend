package org.redex.backend.controller.paquetes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaquetesServiceImp implements PaquetesService {

}
