package org.redex.backend.algorithm;

import java.time.LocalDateTime;
import java.util.Map;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.simulacion.SimulacionPaquete;

public class AlgoritmoPaquete {

    private Long id;

    private AlgoritmoOficina oficinaOrigen;

    private AlgoritmoOficina oficinaDestino;

    private LocalDateTime fechaRegistro;

    public static AlgoritmoPaquete of(SimulacionPaquete o, Map<String, AlgoritmoOficina> oficinas) {
        AlgoritmoPaquete ap = new AlgoritmoPaquete();

        ap.setId(o.getId());
        ap.setOficinaOrigen(oficinas.get(o.getOficinaOrigen().getCodigo()));
        ap.setOficinaDestino(oficinas.get(o.getOficinaDestino().getCodigo()));
        ap.setFechaRegistro(o.getFechaIngreso());

        return ap;
    }

    public static AlgoritmoPaquete of(Paquete o, Map<String, AlgoritmoOficina> oficinas) {
        AlgoritmoPaquete ap = new AlgoritmoPaquete();

        ap.setId(o.getId());
        ap.setOficinaOrigen(oficinas.get(o.getOficinaOrigen().getCodigo()));
        ap.setOficinaDestino(oficinas.get(o.getOficinaDestino().getCodigo()));
        ap.setFechaRegistro(o.getFechaIngreso());
        
        return ap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlgoritmoOficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(AlgoritmoOficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public AlgoritmoOficina getOficinaDestino() {
        return oficinaDestino;
    }

    public void setOficinaDestino(AlgoritmoOficina oficinaDestino) {
        this.oficinaDestino = oficinaDestino;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
