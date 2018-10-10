package org.redex.backend.controller.simulacion;

import java.util.HashMap;
import java.util.Map;

public class VisorSimulacion {

    private final Map<Long, Integer> maximo;

    private final Map<Long, Integer> actual;

    public VisorSimulacion() {
        maximo = new HashMap<>();
        actual = new HashMap<>();
    }

    public synchronized void registrar(Long idSimulacion, Integer cantidadPaquetes) {
        maximo.put(idSimulacion, cantidadPaquetes);
        actual.put(idSimulacion, 0);
    }

    public synchronized void avanzar(Long idSimulacion) {
        actual.replace(idSimulacion, actual.get(idSimulacion) + 1);
    }

    public MensajeSimulacion avance(Long idSimulacion) {
        if (!maximo.containsKey(idSimulacion)) {
            return MensajeSimulacion.crearDesconocido();
        }

        if (actual.get(idSimulacion).equals(maximo.get(idSimulacion))) {
            maximo.remove(idSimulacion);
            actual.remove(idSimulacion);

            return MensajeSimulacion.crearFinalizado();
        }

        return MensajeSimulacion.crearEjecutando(actual.get(idSimulacion), maximo.get(idSimulacion));
    }
}
