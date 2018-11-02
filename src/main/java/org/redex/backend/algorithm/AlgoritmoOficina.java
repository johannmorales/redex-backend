package org.redex.backend.algorithm;

import java.util.Objects;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.model.simulacion.SimulacionOficina;

    public class AlgoritmoOficina {

    private Long id;

    private String codigo;

    private Integer capacidadActual;

    private Integer capacidadMaxima;

    private Integer zonaHoraria;

    public static AlgoritmoOficina of(SimulacionOficina o) {
        AlgoritmoOficina ao = new AlgoritmoOficina();

        ao.setId(o.getId());
        ao.setCodigo(o.getCodigo());
        ao.setCapacidadActual(o.getCapacidadInicial());
        ao.setCapacidadMaxima(o.getCapacidadMaxima());
        ao.setZonaHoraria(o.getZonaHoraria());

        return ao;
    }

    public static AlgoritmoOficina of(Oficina o) {
        AlgoritmoOficina ao = new AlgoritmoOficina();

        ao.setId(o.getId());
        ao.setCapacidadActual(o.getCapacidadActual());
        ao.setCapacidadMaxima(o.getCapacidadMaxima());
        ao.setCodigo(o.getCodigo());
        ao.setZonaHoraria(o.getZonaHoraria());

        return ao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(Integer capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(Integer zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public int getPorcentajeUsado() {
        return this.capacidadActual / this.capacidadMaxima * 100;
    }

}
