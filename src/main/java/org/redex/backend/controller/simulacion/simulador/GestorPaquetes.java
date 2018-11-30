package org.redex.backend.controller.simulacion.simulador;

import com.google.common.collect.TreeMultimap;
import org.redex.backend.controller.simulacion.Ventana;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.redex.backend.model.envios.Paquete;

@Component
public class GestorPaquetes {

    private TreeMultimap<LocalDateTime, Paquete> paquetes = TreeMultimap.create();

    public void agregarLista(List<Paquete> paquetes) {
        for (Paquete paquete : paquetes) {
            this.agregarUno(paquete);
        }
    }

    public void agregarUno(Paquete paquete) {
        this.paquetes.put(paquete.getFechaIngreso(), paquete);
    }

    public void eliminarHasta(LocalDateTime hasta) {
        //paquetes = new TreeMap<>(paquetes.tailMap(hasta));
    }

    public List<Paquete> allEntranVentana(Ventana ventana) {
        List<Paquete> list = paquetes
                .asMap()
                .tailMap(ventana.getInicio(), true)
                .headMap(ventana.getFin(), false)
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return list;
    }

    public void inicializar() {
        this.paquetes = TreeMultimap.create();
    }
}
