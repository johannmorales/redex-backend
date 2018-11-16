/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Oscar
 */
public class GraficosController {
    
    @Autowired
    GraficosService service;
    
    @PostMapping("/graficos/paquetesXoficinaXfecha_linea")
    public void paquetesXoficinaXfecha_linea(){
        service.paquetesXoficinaXfecha_linea(1,"","");
    }
    
    @PostMapping("/graficos/paquetesXvuelosXfecha")
    public void paquetesXvuelosXfecha(){
        service.paquetesXvuelosXfecha("","");
        
    }
    
    @PostMapping("/graficos/paquetesXoficinasXfecha_barra")
    public void paquetesXoficinasXfecha_barra(){
        service.paquetesXoficinasXfecha_barra("","");
    }
}
