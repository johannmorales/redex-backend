package org.redex.backend.controller.simulacion;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionEstadoEnum;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SimulacionServiceImp implements SimulacionService {

    @Override
    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file) {
        //Simulacion simu = simuRepo.findById(id).orElseThrow .... 
        
        //simuPaquete.setSimulacion(simu)
        return null;
    }

    @Override
    public Simulacion crear() {
        Simulacion s = new Simulacion();
        s.setCantidadOficinas(0);
        s.setCantidadPaquetes(0);
        s.setEstado(SimulacionEstadoEnum.INTEGRANDO);
        s.setFechaFin(null);
        s.setFechaInicio(null);
        
        return s;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<Simulacion> crimsonList(CrimsonTableRequest request) {
        return Page.empty();
    }

}
