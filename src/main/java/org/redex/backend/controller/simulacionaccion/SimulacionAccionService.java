package org.redex.backend.controller.simulacionaccion;

import java.util.List;
import org.redex.backend.model.simulacion.SimulacionAccion;

public interface SimulacionAccionService {

    public List<SimulacionAccion> list(Long idSimulacion);
    
}
