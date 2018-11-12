package org.redex.backend.controller.simulacion;

import org.redex.backend.algorithm.AlgoritmoPaquete;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GestorPaquetes {

    private SortedMap<LocalDateTime,List<AlgoritmoPaquete>> paquetes =  new TreeMap<>();

    public void agregarLista(List<AlgoritmoPaquete> paquetes){
        for (AlgoritmoPaquete paquete : paquetes) {
            this.agregarUno(paquete);
        }
    }


    public void agregarUno(AlgoritmoPaquete paquete){
        if(!this.paquetes.containsKey(paquete.getFechaRegistro())){
            this.paquetes.put(paquete.getFechaRegistro(), new ArrayList<>());
        }
        this.paquetes.get(paquete.getFechaRegistro()).add(paquete);
    }

    public void eliminarHasta(LocalDateTime hasta){
        paquetes = new TreeMap<>(paquetes.tailMap(hasta));
    }


    public List<AlgoritmoPaquete> allEntranVentana(Ventana ventana) {
        return paquetes.tailMap(ventana.getInicio()).headMap(ventana.getFin()).values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
