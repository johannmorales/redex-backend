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
public class Linea_POF {

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
     * @return the idOficina
     */
    public int getIdOficina() {
        return idOficina;
    }

    /**
     * @param idOficina the idOficina to set
     */
    public void setIdOficina(int idOficina) {
        this.idOficina = idOficina;
    }

    /**
     * @return the pof
     */
    public List<POF> getPof() {
        return pof;
    }

    /**
     * @param pof the pof to set
     */
    public void setPof(List<POF> pof) {
        this.pof = pof;
    }
   
    private int idOficina;
    private String fechaI;
    private String fechaF;
    private List<POF> pof;
}
