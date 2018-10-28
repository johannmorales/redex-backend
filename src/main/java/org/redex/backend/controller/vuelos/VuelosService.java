package org.redex.backend.controller.vuelos;

import org.redex.backend.model.envios.Vuelo;

public interface VuelosService {
    
    public Vuelo save(Vuelo wrapper);
    
    public Vuelo update(Vuelo wrapper);
    
    public Vuelo find(Long id);
}
