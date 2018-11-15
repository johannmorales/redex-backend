/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

import org.redex.backend.model.general.Archivo;

/**
 *
 * @author Oscar
 */
public interface ReportesService {

    public Archivo paquetesXvuelo(Long id);
    
    public Archivo enviosXfechas(String fI,String fF);
    
    public Archivo paquetesXusuario(Long id);
    
    public Archivo accionesXusuarioXoficinaXfecha();
    
}
