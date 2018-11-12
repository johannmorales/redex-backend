package org.redex.backend.controller.simulacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.AlgoritmoVuelo;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GestorVuelosAgendados {

    private static final Logger logger = LogManager.getLogger(GestorVuelosAgendados.class);

    private LocalDate finGeneracionVuelosAgendados = null;
    private List<AlgoritmoVuelo> vuelos = new ArrayList<>();

    private SortedMap<LocalDateTime, List<AlgoritmoVueloAgendado>> vuelosAgendadosPorInicio = new TreeMap<>();

    private void crearUnDiaVuelosAgendados(LocalDate dia) {
        if (finGeneracionVuelosAgendados != null && finGeneracionVuelosAgendados.isAfter(dia)) {
            return;
        }

        for (AlgoritmoVuelo vuelo : vuelos) {
            LocalDateTime inicio = LocalDateTime.of(dia, vuelo.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(dia, vuelo.getHoraFin());

            if(vuelo.getHoraFin().isBefore(vuelo.getHoraInicio())){
                inicio =  LocalDateTime.of(dia, vuelo.getHoraInicio());
                fin =  LocalDateTime.of(dia.plusDays(1L), vuelo.getHoraFin());
            }
            AlgoritmoVueloAgendado sva = new AlgoritmoVueloAgendado();
            sva.setFechaInicio(inicio);
            sva.setFechaFin(fin);
            sva.setCapacidadActual(0);
            sva.setCantidadSalida(0);
            sva.setCapacidadMaxima(vuelo.getCapacidad());
            sva.setOficinaOrigen(vuelo.getOficinaOrigen());
            sva.setOficinaDestino(vuelo.getOficinaDestino());
            this.agregarVueloAgendado(sva);
        }
        this.finGeneracionVuelosAgendados = dia.plusDays(1L);
    }

    private void agregarVueloAgendado(AlgoritmoVueloAgendado sva) {
        if (!this.vuelosAgendadosPorInicio.containsKey(sva.getFechaInicio())) {
            this.vuelosAgendadosPorInicio.put(sva.getFechaInicio(), new ArrayList<>());
        }
        this.vuelosAgendadosPorInicio.get(sva.getFechaInicio()).add(sva);
    }

    public List<AlgoritmoVueloAgendado> allAlgoritmo(LocalDateTime inicio, LocalDateTime fin){
        return vuelosAgendadosPorInicio.tailMap(inicio).values().stream().flatMap(Collection::stream).filter(va -> va.getFechaFin().isBefore(fin) && va.getCapacidadActual() < va.getCapacidadMaxima()).collect(Collectors.toList());
    }

    public void eliminarHasta(LocalDateTime hasta) {
        System.out.printf("TBD");
    }

    public List<AlgoritmoVueloAgendado> allPartenEnVentana(Ventana ventana) {
        SortedMap<LocalDateTime, List<AlgoritmoVueloAgendado>> submap = this.vuelosAgendadosPorInicio.tailMap(ventana.getInicio());
        return submap.headMap(ventana.getFin()).values().stream().flatMap(Collection::stream).collect(Collectors.toList());
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

    public void setVuelos(List<AlgoritmoVuelo> nuevosVuelos) {
        System.out.println(String.format("Seteando %d vuelos", nuevosVuelos.size()));
        this.vuelos = nuevosVuelos;
    }
}
