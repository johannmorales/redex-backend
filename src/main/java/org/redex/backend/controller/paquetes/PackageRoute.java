/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.paquetes;

import java.math.BigDecimal;

public class PackageRoute {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(String isLocation) {
        this.isLocation = isLocation;
    }

    public BigDecimal getLatF() {
        return latF;
    }

    public void setLatF(BigDecimal latF) {
        this.latF = latF;
    }

    public BigDecimal getLonF() {
        return lonF;
    }

    public void setLonF(BigDecimal lonF) {
        this.lonF = lonF;
    }

    public BigDecimal getLatI() {
        return latI;
    }

    public void setLatI(BigDecimal latI) {
        this.latI = latI;
    }

    public BigDecimal getLonI() {
        return lonI;
    }

    public void setLonI(BigDecimal lonI) {
        this.lonI = lonI;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPaisI() {
        return paisI;
    }

    public void setPaisI(String paisI) {
        this.paisI = paisI;
    }

    public String getPaisF() {
        return paisF;
    }

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

    private String title;

    private String subtitle;

    private String active;

    private String isLocation;

}
