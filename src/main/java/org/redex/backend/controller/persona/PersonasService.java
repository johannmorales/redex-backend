package org.redex.backend.controller.persona;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Archivo;
import org.redex.backend.model.general.Persona;

public interface PersonasService {

    public List<Persona> all();
    
    public CargaDatosResponse carga(Archivo archivo);
    
}
