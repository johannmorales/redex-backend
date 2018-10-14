package org.redex.backend.controller.oficinas;

import org.redex.model.rrhh.Colaborador;
import org.redex.model.rrhh.Oficina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OficinasServiceImp implements OficinasService {

    @Override
    public void cambiarJefe(Oficina oficina, Colaborador colaborador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void agregarColaborador(Colaborador colaborador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
