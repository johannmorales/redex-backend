package org.redex.backend.algorithm;

import java.util.List;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public interface Algoritmo {

    List<SimulacionVueloAgendado> runBatch(List<SimulacionPaquete> paquetes, List<VueloAgendado> vuelosAgendados, List<SimulacionOficina> oficinas);

    List<AlgoritmoVueloAgendado> run(AlgoritmoPaquete paquete, List<AlgoritmoVueloAgendado> vuelosAgendados, List<AlgoritmoVueloAgendado> vuelosSalida, List<AlgoritmoOficina> oficinas);

}
