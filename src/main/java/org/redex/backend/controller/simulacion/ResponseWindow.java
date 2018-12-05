/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.simulacion;

import java.util.List;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;

/**
 *
 * @author Oscar
 */
public class ResponseWindow {

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the termino to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the acciones
     */
    public List<SimulacionAccionWrapper> getListActions() {
        return listActions;
    }

    /**
     * @param acciones the acciones to set
     */
    public void setListActions(List<SimulacionAccionWrapper> listActions) {
        this.listActions = listActions;
    }
    private int status;
    private List<SimulacionAccionWrapper> listActions;
}
