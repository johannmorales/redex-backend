package org.redex.backend.algorithm;

import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public interface Algoritmo {

    List<VueloAgendado> run(Paquete paquete, List<VueloAgendado> vuelosAgendados, List<VueloAgendado> vuelosSalida, List<Oficina> oficinas);

}
