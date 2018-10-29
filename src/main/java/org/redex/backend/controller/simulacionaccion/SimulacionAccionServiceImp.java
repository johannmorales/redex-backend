package org.redex.backend.controller.simulacionaccion;

import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.redex.backend.repository.SimulacionAccionRepository;
import org.redex.backend.repository.SimulacionRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SimulacionAccionServiceImp implements SimulacionAccionService{

    @Autowired
    SimulacionRepository simulacionRepository;
    
    @Autowired
    SimulacionAccionRepository simulacionAccionRepository;
    
    @Override
    public List<SimulacionAccion> list(Long idSimulacion) {
        Simulacion s = simulacionRepository.findById(idSimulacion)
                .orElseThrow(() -> new ResourceNotFoundException("Simulacion", "id", idSimulacion));
        
        return simulacionAccionRepository.findAllBySimulacion(s);
    }
    
}
