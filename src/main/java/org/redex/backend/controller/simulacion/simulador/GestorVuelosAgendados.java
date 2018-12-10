package org.redex.backend.controller.simulacion.simulador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.gestor.AlgoritmoMovimiento;
import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GestorVuelosAgendados {

    @Autowired
    Simulador simulador;

    private static final Logger logger = LogManager.getLogger(GestorVuelosAgendados.class);

    private LocalDate finGeneracionVuelosAgendados;
    private List<Vuelo> vuelos;
    private SortedSimpleList<LocalDateTime, VueloAgendado> vuelosAgendadosPorInicio;
    private SortedSimpleList<LocalDateTime, VueloAgendado> vuelosAgendadosPorFin;

    private SortedSimpleList<LocalDateTime, AlgoritmoMovimiento> movimientos;


    public GestorVuelosAgendados() {
        this.inicializar();
    }

    public void inicializar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio =  SortedSimpleList.create();
        this.vuelosAgendadosPorFin = SortedSimpleList.create();
        movimientos = SortedSimpleList.create();
        this.vuelos = new ArrayList<>();
    }

    public void reiniciar() {
        this.finGeneracionVuelosAgendados = null;
        this.vuelosAgendadosPorInicio =  SortedSimpleList.create();
        this.vuelosAgendadosPorFin = SortedSimpleList.create();
        movimientos = SortedSimpleList.create();

    }

    public void crearVuelosAgendadosNecesarios(Ventana ventana) {
        LocalDate inicio = ventana.getInicio().toLocalDate().minusDays(1L);
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
       movimientos.add(algoritmoMovimiento.getMomento(), algoritmoMovimiento);
    }


   public List<AlgoritmoMovimiento> allMovimientoAlgoritmo(Ventana ventana){
        return movimientos.inWindow(ventana);
   }


    public List<VueloAgendado> allLleganEnVentana(Ventana ventana) {
        return vuelosAgendadosPorFin.inWindow(ventana);

    }

    public List<VueloAgendado> allPartenEnVentana(Ventana ventana) {
        return vuelosAgendadosPorInicio.inWindow(ventana);
    }

    public List<VueloAgendado> allAlgoritmo(Ventana ventana) {
        return vuelosAgendadosPorInicio.inWindow(ventana)
                .stream()
                .filter(va -> va.getCapacidadActual() < va.getCapacidadMaxima())
                .filter(va -> va.getFechaFin().isBefore(ventana.getFin()) || va.getFechaFin().equals(ventana.getFin()))
                .collect(Collectors.toList());
    }


    public void setVuelos(List<Vuelo> nuevosVuelos) {
        this.vuelos = nuevosVuelos;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void limpiarHasta(LocalDateTime fechaLimite) {
        this.vuelosAgendadosPorFin.deleteBeforeOrEqual(fechaLimite);
        this.vuelosAgendadosPorInicio.deleteBeforeOrEqual(fechaLimite);
        this.movimientos.deleteBeforeOrEqual(fechaLimite);
    }
}
