package org.redex.backend.controller.planvuelo;

import org.redex.model.PlanVuelo;
import org.redex.model.Vuelo;

public interface PlanVueloService {

    PlanVuelo cargar();
    
    void guardar(PlanVuelo planVuelo);
    
    void editarVuelo(Vuelo vuelo);
    
    void desactivarVuelo(Vuelo vuelo);
    
    void activarVuelo(Vuelo vuelo);

    void actualizarVuelo(Vuelo vuelo);
}
