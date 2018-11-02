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
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;

public class GestorAlgoritmo {
    
    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);
    
    private List<AlgoritmoOficina> oficinas;
    
    private SortedMap<LocalDateTime, Map<AlgoritmoOficina, List<AlgoritmoVueloAgendado>>> vuelosAgendadosPorOrigen;
    
    private SortedMap<LocalDateTime, Map<AlgoritmoOficina, Integer>> variacionCapacidadAlmacen;
    
    public GestorAlgoritmo(List<AlgoritmoVueloAgendado> planeados, List<AlgoritmoOficina> oficinas) {
        this.vuelosAgendadosPorOrigen = new TreeMap<>();
        this.variacionCapacidadAlmacen = new TreeMap<>();
        this.oficinas = new ArrayList<>(oficinas);
        this.procesarVariacionEnCapacidades(planeados);
        
        for (AlgoritmoVueloAgendado planeado : planeados) {
            this.agregarVueloAgendado(planeado);
        }
        
    }
    
    public List<AlgoritmoVueloAgendado> obtenerValidos(AlgoritmoOficina oficina, LocalDateTime momento) {
        SortedMap<LocalDateTime, Map<AlgoritmoOficina, List<AlgoritmoVueloAgendado>>> submap = vuelosAgendadosPorOrigen.tailMap(momento);
        
        return submap.values().stream()
                .filter(x -> x.containsKey(oficina))
                .flatMap(x -> x.get(oficina).stream())
                .collect(Collectors.toList());
    }
    
    public Integer obtenerCapacidadEnMomento(AlgoritmoOficina oficina, LocalDateTime momento) {
        if (variacionCapacidadAlmacen.containsKey(momento)) {
            return variacionCapacidadAlmacen.get(momento).get(oficina);
        } else {
            LocalDateTime lastKey = variacionCapacidadAlmacen.headMap(momento).lastKey();
            return variacionCapacidadAlmacen.get(lastKey).get(oficina);
        }
        
    }
    
    private void procesarVariacionEnCapacidades(List<AlgoritmoVueloAgendado> planeados) {
        TreeMultiset<MovimientoVuelo> movimientoVuelos = TreeMultiset.create();
        
        for (AlgoritmoVueloAgendado planeado : planeados) {
            movimientoVuelos.add(new MovimientoVuelo(MovimientoVuelo.Tipo.ENTRADA, planeado));
            movimientoVuelos.add(new MovimientoVuelo(MovimientoVuelo.Tipo.SALIDA, planeado));
        }
        
        Map<AlgoritmoOficina, Integer> mapActual = oficinas.stream().collect(Collectors.toMap(x -> x, x -> 0));
        
        for (MovimientoVuelo movimientoVuelo : movimientoVuelos) {
            if (!mapActual.containsKey(movimientoVuelo.getOficina())) {
                mapActual.put(movimientoVuelo.getOficina(), movimientoVuelo.getVariacion());
            } else {
                Integer variacionAntigua = mapActual.get(movimientoVuelo.getOficina());
                mapActual.replace(movimientoVuelo.getOficina(), variacionAntigua + movimientoVuelo.getVariacion());
            }
            
            LocalDateTime momento = movimientoVuelo.getMomento();
            AlgoritmoOficina oficina = movimientoVuelo.getOficina();
            Integer variacion = mapActual.get(oficina);
            
            if (variacionCapacidadAlmacen.containsKey(momento)) {
                Map<AlgoritmoOficina, Integer> variacionesEnMomento = variacionCapacidadAlmacen.get(momento);
                variacionesEnMomento.put(oficina, 0);
            } else {
                Map<AlgoritmoOficina, Integer> variacionesEnMomento = new HashMap<>(mapActual);
                variacionCapacidadAlmacen.put(momento, variacionesEnMomento);
            }
            
            variacionCapacidadAlmacen.get(momento).replace(oficina, variacion);
        }
    }
    
    private void agregarVueloAgendado(AlgoritmoVueloAgendado va) {
        LocalDateTime momento = va.getFechaInicio();
        AlgoritmoOficina oficina = va.getOficinaOrigen();
        
        if (!vuelosAgendadosPorOrigen.containsKey(momento)) {
            vuelosAgendadosPorOrigen.put(momento, new HashMap<>());
        }
        
        if (!vuelosAgendadosPorOrigen.get(momento).containsKey(oficina)) {
            vuelosAgendadosPorOrigen.get(momento).put(oficina, new ArrayList<>());
        }
        
        vuelosAgendadosPorOrigen.get(momento).get(oficina).add(va);
    }
    
}
