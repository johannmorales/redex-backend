/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Oscar
 */
public interface GraficosService {
    
    public ObjectNode paquetesXoficinaXfecha_linea(int id,String fechaI,String fechaF);
    
    public ObjectNode paquetesXvuelosXfecha(String fechaI,String fechaF);
    
    public ObjectNode paquetesXoficinasXfecha_barra(String fechaI,String fechaF);
    
}
