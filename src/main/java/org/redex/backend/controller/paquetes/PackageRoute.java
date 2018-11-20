/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.paquetes;

import java.math.BigDecimal;

/**
 *
 * @author Oscar
 */
public class PackageRoute {

    /**
     * @return the latF
     */
    public BigDecimal getLatF() {
        return latF;
    }

    /**
     * @param latF the latF to set
     */
    public void setLatF(BigDecimal latF) {
        this.latF = latF;
    }

    /**
     * @return the lonF
     */
    public BigDecimal getLonF() {
        return lonF;
    }

    /**
     * @param lonF the lonF to set
     */
    public void setLonF(BigDecimal lonF) {
        this.lonF = lonF;
    }

    /**
     * @return the latI
     */
    public BigDecimal getLatI() {
        return latI;
    }

    /**
     * @param latI the latI to set
     */
    public void setLatI(BigDecimal latI) {
        this.latI = latI;
    }

    /**
     * @return the lonI
     */
    public BigDecimal getLonI() {
        return lonI;
    }

    /**
     * @param lonI the lonI to set
     */
    public void setLonI(BigDecimal lonI) {
        this.lonI = lonI;
    }

    /**
     * @return the orden
     */
    public int getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the fechaInicio
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the paisI
     */
    public String getPaisI() {
        return paisI;
    }

    /**
     * @param paisI the paisI to set
     */
    public void setPaisI(String paisI) {
        this.paisI = paisI;
    }

    

    /**
     * @return the paisF
     */
    public String getPaisF() {
        return paisF;
    }

    /**
     * @param paisF the paisF to set
     */
    public void setPaisF(String paisF) {
        this.paisF = paisF;
    }

    
    private int orden;
    private String estado;
    private String fechaInicio;
    private String fechaFin;
    private String paisI;
    private BigDecimal latI;
    private BigDecimal lonI;
    private String paisF;
    private BigDecimal latF;
    private BigDecimal lonF;
}
