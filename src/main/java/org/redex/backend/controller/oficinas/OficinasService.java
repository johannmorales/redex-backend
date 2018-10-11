package org.redex.backend.controller.oficinas;

import org.redex.model.Colaborador;
import org.redex.model.Oficina;

public interface OficinasService {

    public void cambiarJefe(Oficina oficina, Colaborador colaborador);
    
    public void agregarColaborador(Colaborador colaborador);
    
}
