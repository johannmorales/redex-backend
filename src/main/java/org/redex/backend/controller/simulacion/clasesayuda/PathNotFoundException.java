package org.redex.backend.controller.simulacion.clasesayuda;

import org.redex.model.Paquete;

public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(Paquete paquete) {
        super(String.format("Ruta no encontrada de %s a %s", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo()));
    }

}
