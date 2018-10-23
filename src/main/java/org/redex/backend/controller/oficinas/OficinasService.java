package org.redex.backend.controller.oficinas;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.rrhh.Colaborador;
import org.redex.model.rrhh.Oficina;

public interface OficinasService {

    public void cambiarJefe(Oficina oficina, Colaborador colaborador);

    public void agregarColaborador(Colaborador colaborador);

    public CargaDatosResponse carga(Archivo archivo);

    public List<Oficina> all();

    public void desactivar(Long id);

    public void activar(Long id);
    
}
