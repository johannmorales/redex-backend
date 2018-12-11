package org.redex.backend.controller.simulacion.simulador;

import org.redex.backend.controller.simulacion.Ventana;
import org.redex.backend.model.envios.Paquete;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GestorPaquetes {

    private SortedList<LocalDateTime, Paquete> paquetes;

    public void agregarLista(List<Paquete> paquetes) {
        inicializar();
        for (Paquete pq : paquetes) {
            this.paquetes.add(pq.getFechaIngreso(), pq);
        }
    }

    public List<Paquete> allEntranVentana(Ventana ventana) {
        return paquetes.inWindow(ventana);
    }

    public void inicializar() {
        this.paquetes = SortedList.create();
    }

    public void limpiarHasta(LocalDateTime fin) {
        paquetes.deleteBeforeOrEqual(fin);
    }
}
