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
public interface GraficosService {
    
    public void paquetesXoficinaXfecha_linea(int id,String fechaI,String fechaF);
    
    public void paquetesXvuelosXfecha(String fechaI,String fechaF);
    
    public void paquetesXoficinasXfecha_barra(String fechaI,String fechaF);
    
}
