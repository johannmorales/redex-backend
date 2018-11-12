/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.redex.backend.controller.personas.PersonasService;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.albatross.zelpers.miscelanea.JsonHelper;

/**
 *
 * @author Oscar
 */
public class ReportesController {
    
    @Autowired
    ReportesService service;
    
    @PostMapping("/reportes/paquetesXvuelo")
    public ResponseEntity<?> paquetesXvuelo(@RequestBody VueloAgendado va){
        service.paquetesXvuelo(va.getId());
        return null;
    }
    
    @PostMapping("/reportes/paquetesXusuario")
    public ResponseEntity<?> paquetesXusuario(@RequestBody Persona p){
        service.paquetesXusuario(p.getId());
        return null;
    }
    
    @PostMapping("/reportes/enviosXfechas")
    public ResponseEntity<?> enviosXfechas(@RequestBody RangoFechas r){
        service.enviosXfechas(r.fecha_ini,r.fecha_fin);
        return null;
    }
}
