package org.redex.backend.algorithm.gestor;

import com.google.common.collect.TreeMultiset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class GestorAlgoritmo {

    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);

    private List<Oficina> oficinas;

    private SortedMap<LocalDateTime, Map<Oficina, List<VueloAgendado>>> vuelosAgendadosPorOrigen;

    private SortedMap<LocalDateTime, Map<Oficina, Integer>> variacionCapacidadAlmacen;

    public GestorAlgoritmo(List<VueloAgendado> planeados, List<VueloAgendado> terminan, List<Oficina> oficinas) {
        this.vuelosAgendadosPorOrigen = new TreeMap<>();
        this.variacionCapacidadAlmacen = new TreeMap<>();
        this.oficinas = new ArrayList<>(oficinas);
        this.procesarVariacionEnCapacidades(planeados);

        for (VueloAgendado planeado : planeados) {
            this.agregarVueloAgendado(planeado);
        }
    }

    public List<VueloAgendado> obtenerValidos(Oficina oficina, LocalDateTime momento) {

       // logger.info("Vuelos despues de {}", momento);
        SortedMap<LocalDateTime, Map<Oficina, List<VueloAgendado>>> submap = vuelosAgendadosPorOrigen.tailMap(momento);

     //   submap.values().stream().filter(x -> x.containsKey(oficina)).flatMap(x -> x.get(oficina).stream()).forEach(v -> logger.info("\t{}", v.getFechaInicio()));

        return submap.values().stream()
                .filter(x -> x.containsKey(oficina))
                .flatMap(x -> x.get(oficina).stream())
                .collect(Collectors.toList());
    }

    public Integer obtenerCapacidadEnMomento(Oficina oficina, LocalDateTime momento) {
        if (variacionCapacidadAlmacen.containsKey(momento)) {
            return variacionCapacidadAlmacen.get(momento).get(oficina);
        } else {
            LocalDateTime lastKey = variacionCapacidadAlmacen.headMap(momento).lastKey();
            return variacionCapacidadAlmacen.get(lastKey).get(oficina);
        }

    }

    private void procesarVariacionEnCapacidades(List<VueloAgendado> planeados) {
        TreeMultiset<AlgoritmoMovimiento> movimientoVuelos = TreeMultiset.create();

        for (VueloAgendado planeado : planeados) {
            movimientoVuelos.add(AlgoritmoMovimiento.crearEntradaVuelo(planeado));
            movimientoVuelos.add(AlgoritmoMovimiento.crearSalidaPaquetes(planeado));
        }

        Map<Oficina, Integer> mapActual = oficinas.stream().collect(Collectors.toMap(x -> x, x -> 0));

        for (AlgoritmoMovimiento movimientoVuelo : movimientoVuelos) {
            if (!mapActual.containsKey(movimientoVuelo.getOficina())) {
                mapActual.put(movimientoVuelo.getOficina(), movimientoVuelo.getVariacion());
            } else {
                Integer variacionAntigua = mapActual.get(movimientoVuelo.getOficina());
                mapActual.replace(movimientoVuelo.getOficina(), variacionAntigua + movimientoVuelo.getVariacion());
            }

            LocalDateTime momento = movimientoVuelo.getMomento();
            Oficina oficina = movimientoVuelo.getOficina();
            Integer variacion = mapActual.get(oficina);

            if (variacionCapacidadAlmacen.containsKey(momento)) {
                Map<Oficina, Integer> variacionesEnMomento = variacionCapacidadAlmacen.get(momento);
                variacionesEnMomento.put(oficina, 0);
            } else {
                Map<Oficina, Integer> variacionesEnMomento = new HashMap<>(mapActual);
                variacionCapacidadAlmacen.put(momento, variacionesEnMomento);
            }

            variacionCapacidadAlmacen.get(momento).replace(oficina, variacion);
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
