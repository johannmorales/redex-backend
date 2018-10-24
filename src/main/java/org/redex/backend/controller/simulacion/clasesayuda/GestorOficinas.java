package org.redex.backend.controller.simulacion.clasesayuda;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.redex.backend.model.rrhh.Oficina;

class GestorOficinas {

    private final Map<String, Oficina> mapOficinas;

    public GestorOficinas(List<Oficina> oficinas) {
        mapOficinas = oficinas.stream().collect(Collectors.toMap(Oficina::getCodigo, x -> x));
    }

    public Oficina getOficina(String codigo) {
        return mapOficinas.get(codigo);
    }

    public void ejecutarAccion(Accion accion) {
        Oficina oficina = accion.getOficina();

        switch (accion.getTipo()) {
            case PAQUETE_REGISTRO:
            case VUELO_LLEGADA:
                oficina.setCapacidadActual(oficina.getCapacidadActual() + accion.getCantidad());
                break;
            case VUELO_SALIDA:
            case PAQUETE_SALIDA:
                oficina.setCapacidadActual(oficina.getCapacidadActual() - accion.getCantidad());
                break;
            default:
                throw new AssertionError();
        }

    }

}
