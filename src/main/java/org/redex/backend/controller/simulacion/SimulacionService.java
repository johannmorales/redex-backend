package org.redex.backend.controller.simulacion;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SimulacionService {

    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file);

}
