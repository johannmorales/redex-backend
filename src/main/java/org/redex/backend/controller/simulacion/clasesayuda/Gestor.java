package org.redex.backend.controller.simulacion.clasesayuda;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Gestor {

    private final List<Oficina> oficinas;

    private final List<Vuelo> vuelos;

    private ZonedDateTime momentoActual;

    private GestorOficinas gestorOficinas;

    private GestorVuelos gestorVuelos;

    private SortedMap<ZonedDateTime, List<Accion>> acciones;

    Gestor(List<Oficina> oficinas, List<VueloAgendado> vuelosAgendados, List<Vuelo> vuelos) {
        this.oficinas = oficinas;
        this.vuelos = vuelos;
        this.gestorOficinas = new GestorOficinas(oficinas);
        this.gestorVuelos = new GestorVuelos(vuelosAgendados);
        this.inicializarAcciones(vuelosAgendados);
    }

    // TODO: falta considerar cuando se recogen los paquetes que vienen de BD
    private void inicializarAcciones(List<VueloAgendado> vas) {
        this.acciones = new TreeMap<>();
        for (VueloAgendado va : vas) {
            agregarAccion(new Accion(Accion.Tipo.VUELO_SALIDA, va));
            agregarAccion(new Accion(Accion.Tipo.VUELO_LLEGADA, va));
        }
    }

    public List<VueloAgendado> obtenerVuelosAgendados() {
        return gestorVuelos.vuelosAgendadosValidos(momentoActual);
    }

    public void avanzar(ZonedDateTime momento) {
        this.momentoActual = momento;

        for (Accion accion : accionesHastaMomento(momento)) {
            gestorOficinas.ejecutarAccion(accion);
        }

        this.acciones = new TreeMap<>(this.acciones.tailMap(momento));
        acciones.remove(momento);
    }

    private List<Accion> accionesHastaMomento(ZonedDateTime momento) {
        List<Accion> list1 = acciones.headMap(momento).values().stream().flatMap(x -> x.stream()).collect(Collectors.toList());
        List<Accion> list2 = acciones.containsKey(momento) ? acciones.get(momento).stream().collect(Collectors.toList()) : new ArrayList<>();

        list1.addAll(list2);

        return list1;
    }

    private void agregarAccion(Accion accion) {
        ZonedDateTime key = accion.getMomento();

        if (!acciones.containsKey(key)) {
            acciones.put(key, new ArrayList<>());
        }

        acciones.get(key).add(accion);
    }

    public void commit(Paquete paquete, List<VueloAgendado> ruta) {
        Accion accion;

        ZonedDateTime inicio = paquete.getFechaRegistro();
        accion = new Accion(Accion.Tipo.PAQUETE_REGISTRO, paquete, inicio);
        agregarAccion(accion);

        ZonedDateTime fin = ruta.get(ruta.size() - 1).getFechaInicio();
        accion = new Accion(Accion.Tipo.PAQUETE_SALIDA, paquete, fin);
        agregarAccion(accion);

        for (VueloAgendado vueloAgendado : ruta) {
            if (!gestorVuelos.vueloAgendadoYaContemplado(vueloAgendado)) {
                accion = new Accion(Accion.Tipo.VUELO_SALIDA, vueloAgendado);
                agregarAccion(accion);

                accion = new Accion(Accion.Tipo.VUELO_LLEGADA, vueloAgendado);
                agregarAccion(accion);

                gestorVuelos.agregarVuelo(vueloAgendado);
            }
        }
    }

    public List<Oficina> getOficinas() {
        return oficinas;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

}
