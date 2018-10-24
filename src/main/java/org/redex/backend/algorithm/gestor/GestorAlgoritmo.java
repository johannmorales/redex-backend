package org.redex.backend.algorithm.gestor;

import com.google.common.collect.TreeMultiset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class GestorAlgoritmo {
    
    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);
    
    private List<Oficina> oficinas;
    
    private SortedMap<ZonedDateTime, Map<Oficina, List<VueloAgendado>>> vuelosAgendadosPorOrigen;
    
    private SortedMap<ZonedDateTime, Map<Oficina, Integer>> variacionCapacidadAlmacen;
    
    public GestorAlgoritmo(Integer dias, ZonedDateTime ahora, List<VueloAgendado> planeados, List<Vuelo> vuelos, List<Oficina> oficinas) {
        this.vuelosAgendadosPorOrigen = new TreeMap<>();
        this.variacionCapacidadAlmacen = new TreeMap<>();
        this.oficinas = new ArrayList<>(oficinas);
        this.procesarVariacionEnCapacidades(planeados);
        this.crearVuelosAgendadosFaltantes(dias, ahora, planeados, vuelos);
    }
    
    public List<VueloAgendado> obtenerValidos(Oficina oficina, ZonedDateTime momento) {
        SortedMap<ZonedDateTime, Map<Oficina, List<VueloAgendado>>> submap = vuelosAgendadosPorOrigen.tailMap(momento);
        
        return submap.values().stream()
                .filter(x -> x.containsKey(oficina))
                .flatMap(x -> x.get(oficina).stream())
                .collect(Collectors.toList());
    }
    
    public Integer obtenerCapacidadEnMomento(Oficina oficina, ZonedDateTime momento) {
        if (variacionCapacidadAlmacen.containsKey(momento)) {
            return variacionCapacidadAlmacen.get(momento).get(oficina);
        } else {
            ZonedDateTime lastKey = variacionCapacidadAlmacen.headMap(momento).lastKey();
            return variacionCapacidadAlmacen.get(lastKey).get(oficina);
        }
        
    }
    
    private void procesarVariacionEnCapacidades(List<VueloAgendado> planeados) {
        TreeMultiset<MovimientoVuelo> movimientoVuelos = TreeMultiset.create();
        
        for (VueloAgendado planeado : planeados) {
            movimientoVuelos.add(new MovimientoVuelo(MovimientoVuelo.Tipo.ENTRADA, planeado));
            movimientoVuelos.add(new MovimientoVuelo(MovimientoVuelo.Tipo.SALIDA, planeado));
        }
        
        Map<Oficina, Integer> mapActual = oficinas.stream().collect(Collectors.toMap(x -> x, x -> 0));
        
        for (MovimientoVuelo movimientoVuelo : movimientoVuelos) {
            if (!mapActual.containsKey(movimientoVuelo.getOficina())) {
                mapActual.put(movimientoVuelo.getOficina(), movimientoVuelo.getVariacion());
            } else {
                Integer variacionAntigua = mapActual.get(movimientoVuelo.getOficina());
                mapActual.replace(movimientoVuelo.getOficina(), variacionAntigua + movimientoVuelo.getVariacion());
            }
            
            ZonedDateTime momento = movimientoVuelo.getMomento();
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
    
    private void crearVuelosAgendadosFaltantes(Integer dias, ZonedDateTime ahora, List<VueloAgendado> planeados, List<Vuelo> vuelos) {
        Set<String> codigos = planeados.stream().map(VueloAgendado::getCodigo).collect(Collectors.toSet());
        
        ZonedDateTime fechaInicio = ahora.truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime fechaFin = fechaInicio.plus(dias, ChronoUnit.DAYS);
        
        boolean fin = false;
        while (!fin) {
            
            for (Vuelo vuelo : vuelos) {
                ZonedDateTime fechaVueloAgendado = fechaInicio.truncatedTo(ChronoUnit.DAYS).with(vuelo.getHoraInicio());
                if (fechaVueloAgendado.isBefore(fechaInicio)) {
                    continue;
                }
                VueloAgendado va = new VueloAgendado(vuelo, fechaInicio);
                
                if (va.getFechaInicio().isAfter(fechaFin)) {
                    fin = true;
                    break;
                } else if (!codigos.contains(va.getCodigo()) && va.getFechaFin().isBefore(fechaFin)) {
                    codigos.add(va.getCodigo());
                    this.agregarVueloAgendado(va);
                }
            }
            
            fechaInicio = fechaInicio.truncatedTo(ChronoUnit.DAYS).plusDays(1L);
        }
    }
    
    private void agregarVueloAgendado(VueloAgendado va) {
        ZonedDateTime momento = va.getFechaInicio();
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
