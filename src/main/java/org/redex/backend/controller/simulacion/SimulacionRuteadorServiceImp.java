package org.redex.backend.controller.simulacion;

import org.redex.backend.algorithm.AlgoritmoWrapper;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;
import org.redex.backend.repository.SimulacionOficinasRepository;
import org.redex.backend.repository.SimulacionVueloAgendadoRepository;
import org.redex.backend.repository.SimulacionVuelosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class SimulacionRuteadorServiceImp implements SimulacionRuteadoService {

    @Autowired
    SimulacionOficinasRepository oficinasRepository;

    @Autowired
    SimulacionVuelosRepository vuelosRepository;

    @Autowired
    SimulacionVueloAgendadoRepository vueloAgendadoRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void findRuta(SimulacionPaquete paquete) {
        LocalDateTime inicio = paquete.getFechaIngreso();
        LocalDateTime fin =
                paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()
                        ? inicio.plus(24, ChronoUnit.HOURS)
                        : inicio.plus(48, ChronoUnit.HOURS);

        List<SimulacionOficina> oficinas = oficinasRepository.findAllBySimulacion(paquete.getSimulacion());

        List<SimulacionVueloAgendado> vueloAgendados = vueloAgendadoRepository.findAllAlgoritmo(paquete.getSimulacion(), inicio, fin);

        List<SimulacionVueloAgendado> vuelosTerminan = vueloAgendadoRepository.findAllAlgoritmoTerminan(paquete.getSimulacion(), inicio, fin);

        List<SimulacionVueloAgendado> ruta = AlgoritmoWrapper.simulacionRun(paquete, vueloAgendados, vuelosTerminan, oficinas);

        int n = 0;

        for (SimulacionVueloAgendado step : ruta) {
            n++;
            if (n == ruta.size()) {
                vueloAgendadoRepository.agregarFinalPaquete(step);
            } else {
                vueloAgendadoRepository.agregarPaquete(step);
            }
        }
    }


}
