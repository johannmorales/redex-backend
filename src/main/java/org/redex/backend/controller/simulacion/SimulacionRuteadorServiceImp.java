package org.redex.backend.controller.simulacion;

import org.redex.backend.algorithm.AlgoritmoWrapper;
import org.redex.backend.model.simulacion.*;
import org.redex.backend.repository.SimulacionAccionRepository;
import org.redex.backend.repository.SimulacionOficinasRepository;
import org.redex.backend.repository.SimulacionVueloAgendadoRepository;
import org.redex.backend.repository.SimulacionVuelosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Autowired
    SimulacionAccionRepository accionRepository;


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

    @Override
    public void accionesVuelosSalida(LocalDateTime inicio, LocalDateTime fin, Simulacion s) {
        List<SimulacionVueloAgendado> vueloAgendados = vueloAgendadoRepository.findAllByWindow(inicio, fin, s);
        for (SimulacionVueloAgendado va : vueloAgendados) {
            SimulacionAccion sa = SimulacionAccion.of(va);
            accionRepository.save(sa);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generarVuelos(LocalDateTime inicio, LocalDateTime fin, Simulacion simulacion) {
        LocalDateTime inicioGeneracion;

        if (simulacion.getFechaFin() != null) {
            if(simulacion.getFechaFin().isAfter(fin)){
                return;
            }
            inicioGeneracion = simulacion.getFechaFin();
        } else {
            inicioGeneracion = inicio;
        }

        LocalDateTime finGeneracion = inicioGeneracion.plusHours(94L);

        List<SimulacionVuelo> vuelos = vuelosRepository.findAllBySimulacion(simulacion);
        int dia = 0;

        LocalDateTime current = inicioGeneracion;

        while (true) {
            for (SimulacionVuelo vuelo : vuelos) {
                if (dia > 0 || (vuelo.getHoraInicio().isAfter(inicioGeneracion.toLocalTime()) || vuelo.getHoraInicio().equals(inicioGeneracion.toLocalTime()))) {
                    LocalDateTime fechaIniVuelo = current.with(vuelo.getHoraInicio());
                    LocalDateTime fechaFinVuelo = vuelo.esDeUnDia() ? current.with(vuelo.getHoraFin()) : current.plus(1, ChronoUnit.DAYS).with(vuelo.getHoraFin());

                    if(fechaFinVuelo.isBefore(finGeneracion)){
                        SimulacionVueloAgendado va = new SimulacionVueloAgendado();
                        va.setCantidadSalida(0);
                        va.setCapacidadActual(0);
                        va.setCapacidadMaxima(vuelo.getCapacidad());
                        va.setFechaFin(fechaFinVuelo);
                        va.setFechaInicio(fechaIniVuelo);
                        va.setVuelo(vuelo);
                        va.setSimulacion(vuelo.getSimulacion());
                        vueloAgendadoRepository.save(va);
                    }
                }
            }

            current = current.plusDays(1L);
            if(current.isAfter(finGeneracion)){
                break;
            }
            dia++;
        }

    }


}
