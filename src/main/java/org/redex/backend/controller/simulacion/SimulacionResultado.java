package org.redex.backend.controller.simulacion;

import java.time.ZonedDateTime;
import java.util.List;
import org.redex.model.Oficina;
import org.redex.model.VueloAgendado;

public class SimulacionResultado {
    
    List<VueloAgendado> vuelos;
    
    List<Oficina> oficina;
    
    ZonedDateTime fechaInicio;
    
    ZonedDateTime fechaFin;
    
}
