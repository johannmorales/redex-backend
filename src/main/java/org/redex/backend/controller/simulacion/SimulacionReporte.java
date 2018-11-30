package org.redex.backend.controller.simulacion;

import java.util.List;

public class SimulacionReporte {

    private Long fechaInicial;

    private Long duracionTotal;

    private String almacenColapso;

    private Integer cantidadAumento;

    private List<SimulacionReporteOficina> oficinas;

    public Long getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Long fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Long getDuracionTotal() {
        return duracionTotal;
    }

    public void setDuracionTotal(Long duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public String getAlmacenColapso() {
        return almacenColapso;
    }

    public void setAlmacenColapso(String almacenColapso) {
        this.almacenColapso = almacenColapso;
    }

    public Integer getCantidadAumento() {
        return cantidadAumento;
    }

    public void setCantidadAumento(Integer cantidadAumento) {
        this.cantidadAumento = cantidadAumento;
    }

    public List<SimulacionReporteOficina> getOficinas() {
        return oficinas;
    }

    public void setOficinas(List<SimulacionReporteOficina> simulacionReporteOficina) {
        this.oficinas = simulacionReporteOficina;
    }



}
