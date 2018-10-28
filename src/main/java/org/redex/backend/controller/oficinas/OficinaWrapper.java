package org.redex.backend.controller.oficinas;

import org.redex.backend.model.rrhh.Oficina;

public class OficinaWrapper {
    
    public Long id;
    
    public Long pais;
    
    public Integer capacidad;

    public String codigo;
    
    public static OficinaWrapper of(Oficina oficina){
        OficinaWrapper wrapper = new OficinaWrapper();
        
        wrapper.id = null;
        wrapper.capacidad = oficina.getCapacidadMaxima();
        wrapper.pais = oficina.getPais().getId();
        wrapper.codigo = oficina.getCodigo();
        
        return wrapper;
    }
    
    public Oficina build(){
        Oficina oficina = new Oficina();
        oficina.setId(id);
        return oficina;
    }
    
}
