package org.redex.backend.algorithm.gestor;

import com.google.common.collect.TreeMultimap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.controller.simulacion.simulador.SortedSumList;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorAlgoritmo {

    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);

    private List<Oficina> oficinas;


    private Map<Oficina, SortedSumList<LocalDateTime, Integer>> aumentosCapacidad;
    private Map<Oficina, SortedSumList<LocalDateTime, Integer>> disminucionCapacidad;

    private Map<Oficina, TreeMultimap<LocalDateTime, VueloAgendado>> vuelosAgendadosPorOrigen2;

    public GestorAlgoritmo(List<VueloAgendado> vuelosCumplen, List<VueloAgendado> vuelosParten, List<VueloAgendado> vuelosLlegan, List<Oficina> oficinas) {
        this.vuelosAgendadosPorOrigen2 = new HashMap<>();
        this.aumentosCapacidad = new HashMap<>();
        this.disminucionCapacidad = new HashMap<>();

        this.oficinas = oficinas;

        for (Oficina oficina : this.oficinas) {
            vuelosAgendadosPorOrigen2.put(oficina, TreeMultimap.create());
            aumentosCapacidad.put(oficina, SortedSumList.create());
            disminucionCapacidad.put(oficina, SortedSumList.create());
        }

        Long t1 = System.currentTimeMillis();

        this.procesarVariacionEnCapacidades(vuelosParten, vuelosLlegan);

        Long t2 = System.currentTimeMillis();

        for (VueloAgendado planeado : vuelosCumplen) {
            this.agregarVueloAgendado(planeado);
        }

        Long t3 = System.currentTimeMillis();

        logger.info("\tvariacion en capacidades: {}", t2 - t1);
        logger.info("\tagregando vuelos posible: {}", t3 - t2);

    }

    public GestorAlgoritmo(List<VueloAgendado> vuelosCumplen, List<AlgoritmoMovimiento> movimientos, List<Oficina> oficinas) {
        this.vuelosAgendadosPorOrigen2 = new HashMap<>();
        this.disminucionCapacidad = new HashMap<>();
        this.aumentosCapacidad = new HashMap<>();

        this.oficinas = oficinas;

        for (Oficina oficina : this.oficinas) {
            vuelosAgendadosPorOrigen2.put(oficina, TreeMultimap.create());
            aumentosCapacidad.put(oficina, SortedSumList.create());
            disminucionCapacidad.put(oficina, SortedSumList.create());
        }

        for (AlgoritmoMovimiento movimiento : movimientos) {
            Oficina oficina = movimiento.getOficina();
            if (movimiento.getVariacion() < 0) {
                disminucionCapacidad.get(oficina).add(movimiento.getMomento(), movimiento.getVariacion() * -1);
            } else {
                aumentosCapacidad.get(oficina).add(movimiento.getMomento(), movimiento.getVariacion());
            }
        }

        for (VueloAgendado planeado : vuelosCumplen) {
            this.agregarVueloAgendado(planeado);
        }

    }


    public List<VueloAgendado> obtenerValidos(Oficina oficina, LocalDateTime momento) {
        NavigableMap<LocalDateTime, Collection<VueloAgendado>> map = vuelosAgendadosPorOrigen2.get(oficina).asMap().tailMap(momento, true);
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Integer obtenerCapacidadEnMomento(Oficina oficina, LocalDateTime momento) {
        return aumentosCapacidad.get(oficina).get(momento) - disminucionCapacidad.get(oficina).getLastBefore(momento);
    }

    private void addMovimiento(TreeMap<LocalDateTime, List<AlgoritmoMovimiento>> movimientos, AlgoritmoMovimiento mov) {
        if (!movimientos.containsKey(mov.getMomento())) {
            movimientos.put(mov.getMomento(), new ArrayList<>());
        }
        movimientos.get(mov.getMomento()).add(mov);
    }

    private void procesarVariacionEnCapacidades(List<VueloAgendado> vuelosParten, List<VueloAgendado> vuelosLlegan) {

        TreeMap<LocalDateTime, List<AlgoritmoMovimiento>> movimientos = new TreeMap<>();

        for (VueloAgendado parte : vuelosParten) {
            if (parte.getCapacidadActual() > 0) {
                AlgoritmoMovimiento mov = AlgoritmoMovimiento.crearSalidaVuelo(parte);
                addMovimiento(movimientos, mov);
            }
        }

        for (VueloAgendado llega : vuelosLlegan) {
            if (llega.getCapacidadActual() > 0) {
                AlgoritmoMovimiento algoEntrada = AlgoritmoMovimiento.crearEntradaVuelo(llega);
                AlgoritmoMovimiento algoSalida = AlgoritmoMovimiento.crearSalidaPaquetes(llega);
                if (algoEntrada.getVariacion() != 0) addMovimiento(movimientos, algoEntrada);
                if (algoSalida.getVariacion() != 0) addMovimiento(movimientos, algoSalida);
            }
        }


        for (Map.Entry<LocalDateTime, List<AlgoritmoMovimiento>> entry : movimientos.entrySet()) {
            for (AlgoritmoMovimiento movimiento : entry.getValue()) {
                Oficina oficina = movimiento.getOficina();
                if (movimiento.getVariacion() < 0) {
                    disminucionCapacidad.get(oficina).add(movimiento.getMomento(), movimiento.getVariacion() * -1);
                } else {
                    aumentosCapacidad.get(oficina).add(movimiento.getMomento(), movimiento.getVariacion());
                }
            }
        }

    }


    private void agregarVueloAgendado(VueloAgendado va) {
        LocalDateTime momento = va.getFechaInicio();
        Oficina oficina = va.getOficinaOrigen();
        vuelosAgendadosPorOrigen2.get(oficina).put(momento, va);
    }

}
