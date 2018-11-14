package org.redex.backend.algorithm;

import org.redex.backend.model.envios.Paquete;

public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(Paquete paquete) {
        super(String.format("Ruta no encontrada de %s a %s", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo()));
    }

}
