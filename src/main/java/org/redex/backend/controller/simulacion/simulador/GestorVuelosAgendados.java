package org.redex.backend.controller.simulacion.simulador;

import io.jsonwebtoken.lang.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.controller.simulacion.Ventana;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;

@Component
public class GestorVuelosAgendados {

    private static final Logger logger = LogManager.getLogger(GestorVuelosAgendados.class);

    private LocalDate finGeneracionVuelosAgendados;
    private List<Vuelo> vuelos;
    private TreeMap<LocalDateTime, List<VueloAgendado>> vuelosAgendadosPorInicio;
    private TreeMap<LocalDateTime, List<VueloAgendado>> vuelosAgendadosPorFin;

    public GestorVuelosAgendados() {
        this.inicializar();
    }

    public void inicializar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio = new TreeMap<>();
        this.vuelosAgendadosPorFin = new TreeMap<>();
        this.vuelos = new ArrayList<>();
    }

    public void reiniciar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio = new TreeMap<>();
        this.vuelosAgendadosPorFin = new TreeMap<>();
    }

    private void crearUnDiaVuelosAgendados(LocalDate dia) {
        if (finGeneracionVuelosAgendados != null && finGeneracionVuelosAgendados.isAfter(dia)) {
            return;
        }

        for (Vuelo vuelo : vuelos) {
            LocalDateTime inicio = LocalDateTime.of(dia, vuelo.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(dia, vuelo.getHoraFin());

            if (vuelo.getHoraFin().isBefore(vuelo.getHoraInicio()) || vuelo.getHoraFin().equals(vuelo.getHoraInicio())) {
                inicio = LocalDateTime.of(dia, vuelo.getHoraInicio());
                fin = LocalDateTime.of(dia.plusDays(1L), vuelo.getHoraFin());
            }

            VueloAgendado sva = new VueloAgendado();
            sva.setFechaInicio(inicio);
            sva.setFechaFin(fin);
            sva.setCapacidadActual(0);
            sva.setCantidadSalida(0);
            sva.setCapacidadMaxima(vuelo.getCapacidad());
            sva.setVuelo(vuelo);
            this.agregarVueloAgendado(sva);
        }

        this.vuelosAgendadosPorInicio.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .forEach((va) -> Assert.isTrue(va.getFechaFin().isAfter(va.getFechaInicio()) || va.getFechaFin().equals(va.getFechaInicio())));

        this.finGeneracionVuelosAgendados = dia.plusDays(1L);
    }

    private void agregarVueloAgendado(VueloAgendado sva) {
        if (!this.vuelosAgendadosPorInicio.containsKey(sva.getFechaInicio())) {
            this.vuelosAgendadosPorInicio.put(sva.getFechaInicio(), new ArrayList<>());
        }

        this.vuelosAgendadosPorInicio.get(sva.getFechaInicio()).add(sva);

        if (!this.vuelosAgendadosPorFin.containsKey(sva.getFechaFin())) {
            this.vuelosAgendadosPorFin.put(sva.getFechaFin(), new ArrayList<>());
        }

        this.vuelosAgendadosPorFin.get(sva.getFechaFin()).add(sva);
    }

    public List<VueloAgendado> allAlgoritmo(LocalDateTime inicio, LocalDateTime fin) {
        return vuelosAgendadosPorInicio.tailMap(inicio).values()
                .stream()
                .flatMap(Collection::stream)
                .filter(va -> va.getFechaFin().isBefore(fin) )
                .collect(Collectors.toList());
    }

    public void eliminarHasta(LocalDateTime hasta) {

    }

    public List<VueloAgendado> allLleganEnVentana(Ventana ventana) {
        SortedMap<LocalDateTime, List<VueloAgendado>> submap;

        if (ventana.getInicio() != null) {
            submap = this.vuelosAgendadosPorFin.tailMap(ventana.getInicio());
        } else {
            submap = this.vuelosAgendadosPorFin;
        }

        List<VueloAgendado> vueloAgendados = submap.headMap(ventana.getFin()).values()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(VueloAgendado::getFechaFin))
                .collect(Collectors.toList());

        for (VueloAgendado vueloAgendado : vueloAgendados) {
            Assert.isTrue(vueloAgendado.getFechaFin().isBefore(ventana.getFin()));
        }

        return vueloAgendados;
    }

    public List<VueloAgendado> allPartenEnVentana(Ventana ventana) {
        SortedMap<LocalDateTime, List<VueloAgendado>> submap;
        if (ventana.getInicio() != null) {
            submap = this.vuelosAgendadosPorInicio.tailMap(ventana.getInicio());
        } else {
            submap = this.vuelosAgendadosPorInicio;
        }
        return submap.headMap(ventana.getFin()).values()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(VueloAgendado::getFechaInicio))
                .collect(Collectors.toList());
    }

    public void crearVuelosAgendadosNecesarios(Ventana ventana) {
        LocalDate inicio = ventana.getInicio().toLocalDate();
        LocalDate fin = ventana.getFin().toLocalDate().plusDays(4L);

        if (this.finGeneracionVuelosAgendados != null) {
            if (fin.isBefore(this.finGeneracionVuelosAgendados)) {
                return;
            } else {
                inicio = finGeneracionVuelosAgendados;
            }
        }

        while (true) {
            this.crearUnDiaVuelosAgendados(inicio);
            inicio = inicio.plusDays(1L);
            if (inicio.equals(fin) || inicio.isAfter(fin)) {
                break;
            }
        }
        this.finGeneracionVuelosAgendados = fin;
    }


    public void setVuelos(List<Vuelo> nuevosVuelos) {
        this.vuelos = nuevosVuelos;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }
}
