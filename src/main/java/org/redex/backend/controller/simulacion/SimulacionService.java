package org.redex.backend.controller.simulacion;

import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface SimulacionService {

    CargaDatosResponse cargaPaquetes(Long id, MultipartFile file);
    
    CargaDatosResponse cargaVuelos(Long id, MultipartFile file);
    
    CargaDatosResponse cargaOficinas(Long id, MultipartFile file);

    Simulacion crear();

    void eliminar(Long id);

    Page<Simulacion> crimsonList(CrimsonTableRequest request);

    List<SimulacionAccion> accionesByWindow(WindowRequest request);

    List<SimulacionOficina> listOficinas(Long id);

    void resetear(Long id);
    
}
