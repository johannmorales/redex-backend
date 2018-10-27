package org.redex.backend.controller.simulacion;

import org.redex.backend.zelper.response.CargaDatosResponse;
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

}
