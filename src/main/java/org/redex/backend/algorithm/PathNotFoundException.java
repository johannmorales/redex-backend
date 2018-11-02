package org.redex.backend.algorithm;

public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(AlgoritmoPaquete paquete) {
        super(String.format("Ruta no encontrada de %s a %s", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo()));
    }

}
