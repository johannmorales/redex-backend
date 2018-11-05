package org.redex.backend.controller.simulacion;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionPaquete;

import java.time.LocalDateTime;

public interface SimulacionRuteadoService {

    void findRuta(SimulacionPaquete paquete);

    void accionesVuelosSalida(LocalDateTime inicio, LocalDateTime fin, Simulacion s);
}
