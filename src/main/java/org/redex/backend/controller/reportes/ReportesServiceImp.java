/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oscar
 */
@Service
@Transactional(readOnly = true)
public class ReportesServiceImp implements ReportesService{
 
    @Override
    public void paquetesXvuelo(){
        
    }
    
    @Override    
    public void enviosXfechas(){
        
    }
    
    @Override    
    public void paquetesXusuario(){
        
    }
    
    @Override    
    public void accionesXusuarioXoficinaXfecha(){
        
    }
}
