package org.redex.backend.algorithm.gestor;

import com.google.common.collect.TreeMultiset;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.controller.simulacion.simulador.Movimiento;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import pe.albatross.zelpers.miscelanea.ObjectUtil;

public class GestorAlgoritmo {

    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);

    private List<Oficina> oficinas;

    private HashMap<Oficina, TreeMap<LocalDateTime, Integer>> variacionCapacidadAlmacen;

    private SortedMap<LocalDateTime, Map<Oficina, List<VueloAgendado>>> vuelosAgendadosPorOrigen;

//    private SortedMap<LocalDateTime, Map<Oficina, Integer>> variacionCapacidadAlmacen;

    public GestorAlgoritmo(List<VueloAgendado> vuelosCumplen, List<VueloAgendado> vuelosParten, List<VueloAgendado> vuelosLlegan, List<Oficina> oficinas) {
        this.vuelosAgendadosPorOrigen = new TreeMap<>();
        this.variacionCapacidadAlmacen = new HashMap<>();
        this.oficinas = new ArrayList<>(oficinas);

        Long t1 = System.currentTimeMillis();

        this.procesarVariacionEnCapacidades(vuelosParten, vuelosLlegan);

        Long t2 = System.currentTimeMillis();

        for (VueloAgendado planeado : vuelosCumplen) {
            this.agregarVueloAgendado(planeado);
        }

        Long t3 = System.currentTimeMillis();

//        logger.info("variaciones {} ms", t2-t1);
//        logger.info("agregar     {} ms", t3-t2);

    }

    public List<VueloAgendado> obtenerValidos(Oficina oficina, LocalDateTime momento) {
        SortedMap<LocalDateTime, Map<Oficina, List<VueloAgendado>>> submap = vuelosAgendadosPorOrigen.tailMap(momento);

        return submap.values().stream()
                .filter(x -> x.containsKey(oficina))
                .flatMap(x -> x.get(oficina).stream())
                .collect(Collectors.toList());
    }

    public Integer obtenerCapacidadEnMomento(Oficina oficina, LocalDateTime momento) {
        TreeMap<LocalDateTime, Integer> variaciones = variacionCapacidadAlmacen.get(oficina);
        NavigableMap<LocalDateTime, Integer> limitado = variaciones.headMap(momento, true);

        if (limitado.isEmpty()) {
            return 0;
        }

        return limitado.lastEntry().getValue();

    }

    private void addMovimiento( TreeMap<LocalDateTime, List<AlgoritmoMovimiento>> movimientos,AlgoritmoMovimiento mov){
        if(!movimientos.containsKey(mov.getMomento())){
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
                addMovimiento(movimientos, algoEntrada);
                addMovimiento(movimientos, algoSalida);
            }
        }


        List<AlgoritmoMovimiento> movimientosList = movimientos.values()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(AlgoritmoMovimiento::getMomento))
                .collect(Collectors.toList());


        for (Oficina oficina : oficinas) {
            this.variacionCapacidadAlmacen.put(oficina, new TreeMap<>());
        }

        for (AlgoritmoMovimiento movimiento : movimientosList) {
            TreeMap<LocalDateTime, Integer> variaciones = this.variacionCapacidadAlmacen.get(movimiento.getOficina());

            if (variaciones.containsKey(movimiento.getMomento())) {
                Integer actual = variaciones.get(movimiento.getMomento());
                variaciones.replace(movimiento.getMomento(), actual + movimiento.getVariacion());
            } else {
                Integer last = this.obtenerCapacidadEnMomento(movimiento.getOficina(), movimiento.getMomento());
                variaciones.put(movimiento.getMomento(), last + movimiento.getVariacion());
            }
        }

    }

    private void agregarVueloAgendado(VueloAgendado va) {
        LocalDateTime momento = va.getFechaInicio();
        Oficina oficina = va.getOficinaOrigen();

        if (!vuelosAgendadosPorOrigen.containsKey(momento)) {
            vuelosAgendadosPorOrigen.put(momento, new HashMap<>());
        }

        if (!vuelosAgendadosPorOrigen.get(momento).containsKey(oficina)) {
            vuelosAgendadosPorOrigen.get(momento).put(oficina, new ArrayList<>());
        }

        vuelosAgendadosPorOrigen.get(momento).get(oficina).add(va);
    }

}
