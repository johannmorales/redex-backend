package org.redex.backend.controller.paquetes;

import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PaquetesService {

    List<Paquete> list();

    Paquete find(Long id);

    CargaDatosResponse carga(MultipartFile file);

    void save(Paquete paquete);

    Page<Paquete> crimsonList(CrimsonTableRequest request, DataSession ds);
}
