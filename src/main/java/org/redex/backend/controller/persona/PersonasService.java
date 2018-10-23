package org.redex.backend.controller.persona;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.general.Persona;

public interface PersonasService {

    public List<Persona> all();
    
    public CargaDatosResponse carga(Archivo archivo);
    
}
