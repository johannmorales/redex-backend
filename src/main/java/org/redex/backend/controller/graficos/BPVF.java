/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

/**
 *
 * @author Oscar
 */
class BPVF {

    /**
     * @return the sumCap
     */
    public int getSumCap() {
        return sumCap;
    }

    /**
     * @param sumCap the sumCap to set
     */
    public void setSumCap(int sumCap) {
        this.sumCap = sumCap;
    }

    /**
     * @return the oficinaI
     */
    public String getOficinaI() {
        return oficinaI;
    }

    /**
     * @param oficinaI the oficinaI to set
     */
    public void setOficinaI(String oficinaI) {
        this.oficinaI = oficinaI;
    }

    /**
     * @return the oficinaF
     */
    public String getOficinaF() {
        return oficinaF;
    }

    /**
     * @param oficinaF the oficinaF to set
     */
    public void setOficinaF(String oficinaF) {
        this.oficinaF = oficinaF;
    }

    /**
     * @return the horaI
     */
    public String getHoraI() {
        return horaI;
    }

    /**
     * @param horaI the horaI to set
     */
    public void setHoraI(String horaI) {
        this.horaI = horaI;
    }
    private int sumCap;
    private String oficinaI;
    private String oficinaF;
    private String horaI;
}
