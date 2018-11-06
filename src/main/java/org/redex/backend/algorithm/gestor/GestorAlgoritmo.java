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
import org.redex.backend.model.envios.VueloAgendado;

public class GestorAlgoritmo {
    
    private static Logger logger = LogManager.getLogger(GestorAlgoritmo.class);
    
    private List<AlgoritmoOficina> oficinas;
    
    private SortedMap<LocalDateTime, Map<AlgoritmoOficina, List<AlgoritmoVueloAgendado>>> vuelosAgendadosPorOrigen;
    
    private SortedMap<LocalDateTime, Map<AlgoritmoOficina, Integer>> variacionCapacidadAlmacen;
    
    public GestorAlgoritmo(List<AlgoritmoVueloAgendado> planeados, List<AlgoritmoVueloAgendado> vuelosSalida, List<AlgoritmoOficina> oficinas) {
        this.vuelosAgendadosPorOrigen = new TreeMap<>();
        this.variacionCapacidadAlmacen = new TreeMap<>();
        this.oficinas = new ArrayList<>(oficinas);
        this.procesarVariacionEnCapacidades(planeados, vuelosSalida);
        
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
            return oficina.getCapacidadActual() + variacionCapacidadAlmacen.get(momento).get(oficina);
        } else {
            LocalDateTime lastKey = variacionCapacidadAlmacen.headMap(momento).lastKey();
            return oficina.getCapacidadActual() + variacionCapacidadAlmacen.get(lastKey).get(oficina);
        }
        
    }
    
    private void procesarVariacionEnCapacidades(List<AlgoritmoVueloAgendado> planeados, List<AlgoritmoVueloAgendado> vuelosTerminados) {
        TreeMultiset<AlgoritmoMovimiento> movimientos = TreeMultiset.create();
        for (AlgoritmoVueloAgendado v : vuelosTerminados) {
            movimientos.add(AlgoritmoMovimiento.crearEntradaVuelo(v));
            movimientos.add(AlgoritmoMovimiento.crearSalidaPaquetes(v));
        }
        for (AlgoritmoVueloAgendado planeado : planeados) {
            movimientos.add(AlgoritmoMovimiento.crearEntradaVuelo(planeado));
            movimientos.add(AlgoritmoMovimiento.crearSalidaVuelo(planeado));
            movimientos.add(AlgoritmoMovimiento.crearSalidaPaquetes(planeado));
        }
        
        Map<AlgoritmoOficina, Integer> mapAcumulador = oficinas.stream().collect(Collectors.toMap(x -> x, x -> 0));

        for (AlgoritmoMovimiento movimiento : movimientos) {
            if (!mapAcumulador.containsKey(movimiento.getOficina())) {
                mapAcumulador.put(movimiento.getOficina(), movimiento.getVariacion());
            } else {
                Integer variacionAntigua = mapAcumulador.get(movimiento.getOficina());
                mapAcumulador.replace(movimiento.getOficina(), variacionAntigua + movimiento.getVariacion());
            }
            
            LocalDateTime momento = movimiento.getMomento();
            AlgoritmoOficina oficina = movimiento.getOficina();
            Integer variacion = mapAcumulador.get(oficina);
            
            if (variacionCapacidadAlmacen.containsKey(momento)) {
                Map<AlgoritmoOficina, Integer> variacionesEnMomento = variacionCapacidadAlmacen.get(momento);
                if(!variacionesEnMomento.containsKey(oficina)){
                    variacionesEnMomento.put(oficina, variacion);
                }else{
                    variacionesEnMomento.replace(oficina, variacion);
                }

            } else {
                Map<AlgoritmoOficina, Integer> variacionesEnMomento = new HashMap<>(mapAcumulador);
                variacionCapacidadAlmacen.put(momento, variacionesEnMomento);
            }
            
        }
    }
    
    private void agregarVueloAgendado(AlgoritmoVueloAgendado va) {
        LocalDateTime momento = va.getFechaInicio();
        AlgoritmoOficina oficina = va.getOficinaOrigen();

        if(oficina.getCodigo().equals("BIKF") && va.getOficinaDestino().getCodigo().equals("SLLP")) {
            System.out.println("HOLA MUNDO");
        }
        if (!vuelosAgendadosPorOrigen.containsKey(momento)) {
            vuelosAgendadosPorOrigen.put(momento, new HashMap<>());
        }
        
        if (!vuelosAgendadosPorOrigen.get(momento).containsKey(oficina)) {
            vuelosAgendadosPorOrigen.get(momento).put(oficina, new ArrayList<>());
        }
        
        vuelosAgendadosPorOrigen.get(momento).get(oficina).add(va);
    }
    
}
