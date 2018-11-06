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

    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file);
    
    public CargaDatosResponse cargaVuelos(Long id, MultipartFile file);
    
    public CargaDatosResponse cargaOficinas(Long id, MultipartFile file);

    public Simulacion crear();

    public void eliminar(Long id);

    public Page<Simulacion> crimsonList(CrimsonTableRequest request);

    public List<SimulacionAccion> accionesByWindow(WindowRequest request);

    List<SimulacionOficina> listOficinas(Long id);
}
