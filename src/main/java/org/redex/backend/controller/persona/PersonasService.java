package org.redex.backend.controller.persona;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Persona;
import org.springframework.web.multipart.MultipartFile;

public interface PersonasService {

    public List<Persona> all();
    
    public CargaDatosResponse carga(MultipartFile file);

    public Persona find(Long id);
    
}
