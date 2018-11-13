package org.redex.backend.controller.simulacion;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.Paquete;

@Component
public class GestorPaquetes {

    private final Logger logger = LogManager.getLogger(GestorPaquetes.class);

    private SortedMap<LocalDateTime, List<Paquete>> paquetes = new TreeMap<>();

    public void agregarLista(List<Paquete> paquetes) {
        for (Paquete paquete : paquetes) {
            this.agregarUno(paquete);
        }

        logger.info("{} paquetes agregados", paquetes.size());

    }

    public void agregarUno(Paquete paquete) {
        if (!this.paquetes.containsKey(paquete.getFechaIngreso())) {
            this.paquetes.put(paquete.getFechaIngreso(), new ArrayList<>());
        }
        this.paquetes.get(paquete.getFechaIngreso()).add(paquete);
    }

    public void eliminarHasta(LocalDateTime hasta) {
        paquetes = new TreeMap<>(paquetes.tailMap(hasta));
    }

    public List<Paquete> allEntranVentana(Ventana ventana) {
        return paquetes.tailMap(ventana.getInicio()).headMap(ventana.getFin()).values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
