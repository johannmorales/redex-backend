package org.redex.backend.controller.simulacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.AlgoritmoWrapper;
import org.redex.backend.model.simulacion.*;
import org.redex.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class SimulacionRuteadorServiceImp implements SimulacionRuteadoService {

    @Autowired
    SimulacionRepository simulacionRepository;

    @Autowired
    SimulacionOficinasRepository oficinasRepository;

    @Autowired
    SimulacionVuelosRepository vuelosRepository;

    @Autowired
    SimulacionVueloAgendadoRepository vueloAgendadoRepository;

    @Autowired
    SimulacionAccionRepository accionRepository;


    public static final Logger logger = LogManager.getLogger(SimulacionRuteadorServiceImp.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void findRuta(SimulacionPaquete paquete, List<SimulacionOficina> oficinas) {
        long t1 = System.nanoTime();
        LocalDateTime inicio = paquete.getFechaIngreso();
        LocalDateTime fin =
                paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()
                        ? inicio.plus(24, ChronoUnit.HOURS)
                        : inicio.plus(48, ChronoUnit.HOURS);


        List<SimulacionVueloAgendado> vueloAgendados = vueloAgendadoRepository.findAllAlgoritmo(paquete.getSimulacion(), inicio, fin);

        List<SimulacionVueloAgendado> vuelosTerminan = vueloAgendadoRepository.findAllAlgoritmoTerminan(paquete.getSimulacion(), inicio, fin);

        long t2 = System.nanoTime();

        List<SimulacionVueloAgendado> ruta = AlgoritmoWrapper.simulacionRun(paquete, vueloAgendados, vuelosTerminan, oficinas);
        long t3 = System.nanoTime();

        logger.info("\t\t\t[TOTAL BASE DE DATOS] ({} ms)", (t2 - t1) / 1000000);
        logger.info("\t\t\t[TOTAL ALGORITMO] ({} ms)", (t3 - t2) / 1000000);

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
        accionRepository.generarAccionesVueloAgendado(inicio, fin, s);
        vueloAgendadoRepository.marcarAccionGenerada(inicio, fin, s);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generarVuelos(LocalDateTime inicio, LocalDateTime fin, Simulacion simulacion) {
        LocalDate inicioGeneracion;

        LocalDate finVuelosNecesarios = fin.toLocalDate().plusDays(4);

        if (simulacion.getFechaFin() != null) {
            if (finVuelosNecesarios.isBefore(simulacion.getFechaFin())) {
                return;
            }
            inicioGeneracion = simulacion.getFechaFin();
        } else {
            inicioGeneracion = inicio.toLocalDate();
        }

        LocalDate finGeneracion = finVuelosNecesarios;


        simulacion.setFechaFin(finGeneracion);
        simulacionRepository.save(simulacion);

        logger.info("Generando vuelos de simulacion {} -> {} ", inicioGeneracion, finGeneracion);

        while (true) {
            vueloAgendadoRepository.generarVuelos(inicioGeneracion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), simulacion.getId());
            inicioGeneracion = inicioGeneracion.plusDays(1L);
            if (inicioGeneracion.isAfter(finGeneracion) || inicioGeneracion.isEqual(finGeneracion)) {
                break;
            }
        }

    }


}
