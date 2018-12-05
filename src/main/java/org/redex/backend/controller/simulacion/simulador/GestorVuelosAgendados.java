package org.redex.backend.controller.simulacion.simulador;

import com.google.common.collect.TreeMultimap;
import io.jsonwebtoken.lang.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.gestor.AlgoritmoMovimiento;
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
    private SortedList<LocalDateTime, VueloAgendado> vuelosAgendadosPorInicio;
    private SortedList<LocalDateTime, VueloAgendado> vuelosAgendadosPorFin;
    private SortedList<LocalDateTime, AlgoritmoMovimiento> algoritmoMovimiento;

    public GestorVuelosAgendados() {
        this.inicializar();
    }

    public void inicializar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio =  SortedList.create();
        this.vuelosAgendadosPorFin = SortedList.create();
        this.algoritmoMovimiento = SortedList.create();
        this.vuelos = new ArrayList<>();
    }

    public void reiniciar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio =  SortedList.create();
        this.vuelosAgendadosPorFin = SortedList.create();
        this.algoritmoMovimiento = SortedList.create();
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

    private void crearUnDiaVuelosAgendados(LocalDate dia) {
        if (finGeneracionVuelosAgendados != null && finGeneracionVuelosAgendados.isAfter(dia)) {
            return;
        }

        for (Vuelo vuelo : vuelos) {
            LocalDateTime inicio = LocalDateTime.of(dia, vuelo.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(dia, vuelo.getHoraFin());

            if (vuelo.getHoraFin().isBefore(vuelo.getHoraInicio()) || vuelo.getHoraFin().equals(vuelo.getHoraInicio())) {
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

            AlgoritmoMovimiento movSalida = AlgoritmoMovimiento.crearSalidaPaquetes(sva);
            AlgoritmoMovimiento movPartida = AlgoritmoMovimiento.crearSalidaVuelo(sva);
            AlgoritmoMovimiento movLlegada = AlgoritmoMovimiento.crearEntradaVuelo(sva);

            this.agregarMovimientoAlgoritmo(movSalida);
            this.agregarMovimientoAlgoritmo(movPartida);
            this.agregarMovimientoAlgoritmo(movLlegada);
        }

        this.finGeneracionVuelosAgendados = dia.plusDays(1L);
    }

    private void agregarVueloAgendado(VueloAgendado sva) {
        this.vuelosAgendadosPorInicio.add(sva.getFechaInicio(), sva);
        this.vuelosAgendadosPorFin.add(sva.getFechaFin(), sva);
    }

    public void agregarMovimientoAlgoritmo(AlgoritmoMovimiento algoritmoMovimiento) {
        this.algoritmoMovimiento.add(algoritmoMovimiento.getMomento(), algoritmoMovimiento);
    }

    public List<AlgoritmoMovimiento> allMovimientoAlgoritmo(Ventana window) {
        return this.algoritmoMovimiento.inWindow(window);
    }

    public List<VueloAgendado> allLleganEnVentana(Ventana ventana) {
        return this.vuelosAgendadosPorFin.inWindow(ventana);

    }

    public List<VueloAgendado> allPartenEnVentana(Ventana ventana) {
        return vuelosAgendadosPorInicio.inWindow(ventana);
    }


    public void setVuelos(List<Vuelo> nuevosVuelos) {
        this.vuelos = nuevosVuelos;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }
}
