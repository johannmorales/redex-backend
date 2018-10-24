package org.redex.backend.controller.simulacion.clasesayuda;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.redex.backend.model.envios.VueloAgendado;

public final class GestorVuelos {

    private Set<VueloAgendado> setVuelosAgendados;
    private SortedMap<ZonedDateTime, List<VueloAgendado>> mapVuelosAgendados;

    public GestorVuelos(List<VueloAgendado> vas) {
        setVuelosAgendados = new HashSet<>();
        mapVuelosAgendados = new TreeMap<>();
        for (VueloAgendado va : vas) {
            agregarVuelo(va);
        }
    }

    public void avanzar(ZonedDateTime momento) {
        mapVuelosAgendados = mapVuelosAgendados.tailMap(momento);
    }

    public List<VueloAgendado> vuelosAgendadosValidos(ZonedDateTime momento) {
        return mapVuelosAgendados.tailMap(momento).entrySet().stream().flatMap(x -> x.getValue().stream()).collect(Collectors.toList());
    }

    public void agregarVuelos(List<VueloAgendado> vueloAgendados) {
        for (VueloAgendado vueloAgendado : vueloAgendados) {
            agregarVuelo(vueloAgendado);
        }
    }

    public boolean vueloAgendadoYaContemplado(VueloAgendado va) {
        return setVuelosAgendados.contains(va);
    }

    void agregarVuelo(VueloAgendado vueloAgendado) {
        ZonedDateTime key = vueloAgendado.getFechaInicio();
        if (!mapVuelosAgendados.containsKey(key)) {
            mapVuelosAgendados.put(key, new ArrayList<>());
        }
        mapVuelosAgendados.get(key).add(vueloAgendado);
        setVuelosAgendados.add(vueloAgendado);
    }
}
