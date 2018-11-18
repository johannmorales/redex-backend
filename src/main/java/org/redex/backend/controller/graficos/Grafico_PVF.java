/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import java.util.List;

/**
 *
 * @author Oscar
 */
public class Grafico_PVF {

    /**
     * @return the fechaI
     */
    public String getFechaI() {
        return fechaI;
    }

    /**
     * @param fechaI the fechaI to set
     */
    public void setFechaI(String fechaI) {
        this.fechaI = fechaI;
    }

    /**
     * @return the fechaF
     */
    public String getFechaF() {
        return fechaF;
    }

    /**
     * @param fechaF the fechaF to set
     */
    public void setFechaF(String fechaF) {
        this.fechaF = fechaF;
    }

    /**
     * @return the pvf
     */
    public List<PVF> getPvf() {
        return pvf;
    }

    /**
     * @param pvf the pvf to set
     */
    public void setPvf(List<PVF> pvf) {
        this.pvf = pvf;
    }
    private String fechaI;
    private String fechaF;
    private List<PVF> pvf;
}
