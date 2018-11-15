package org.redex.backend.controller.simulacion;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SimulacionService {

    CargaDatosResponse cargaPaquetes(MultipartFile file);
    
    CargaDatosResponse cargaVuelos(MultipartFile file);
    
    CargaDatosResponse cargaOficinas(MultipartFile file);

    void crear();

}
