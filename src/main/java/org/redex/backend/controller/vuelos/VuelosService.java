package org.redex.backend.controller.vuelos;

import org.redex.backend.controller.planvuelo.VueloWrapper;
import org.redex.backend.model.envios.Vuelo;

public interface VuelosService {
    
    public Vuelo save(VueloWrapper wrapper);
    
    public Vuelo update(VueloWrapper wrapper);
    
    public Vuelo find(Long id);
}
