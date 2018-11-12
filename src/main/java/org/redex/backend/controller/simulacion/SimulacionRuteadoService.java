package org.redex.backend.controller.simulacion;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;

import java.time.LocalDateTime;
import java.util.List;

public interface SimulacionRuteadoService {

    void findRuta(SimulacionPaquete paquete, List<SimulacionOficina> oficinas);

    void accionesVuelosSalida(LocalDateTime inicio, LocalDateTime fin, Simulacion s);

    void generarVuelos(LocalDateTime inicio, LocalDateTime fin, Simulacion simulacion);
}
