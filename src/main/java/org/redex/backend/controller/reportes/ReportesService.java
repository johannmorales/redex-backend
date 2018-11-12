/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

/**
 *
 * @author Oscar
 */
public interface ReportesService {

    public void paquetesXvuelo(Long id);
    
    public void enviosXfechas(String fI,String fF);
    
    public void paquetesXusuario(Long id);
    
    public void accionesXusuarioXoficinaXfecha();
    
}
