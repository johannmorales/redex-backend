package org.redex.backend.controller.simulacion;

import java.time.ZonedDateTime;
import java.util.List;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class SimulacionResultado {

    List<VueloAgendado> vuelos;

    List<Oficina> oficina;

    ZonedDateTime fechaInicio;

    ZonedDateTime fechaFin;

}
