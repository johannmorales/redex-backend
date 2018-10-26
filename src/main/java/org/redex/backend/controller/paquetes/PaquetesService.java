package org.redex.backend.controller.paquetes;

import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PaquetesService {

    public List<Paquete> list();

    public Paquete find(Long id);

    public CargaDatosResponse carga(MultipartFile file);
    
}
