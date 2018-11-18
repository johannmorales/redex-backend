/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.redex.backend.controller.reportes.RangoFechas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Oscar
 */
@RestController
public class GraficosController {
    
    @Autowired
    GraficosService service;
    
    @PostMapping("/graficos/paquetesXoficinaXfecha_linea")
    public ResponseEntity<?> paquetesXoficinaXfecha_linea(@RequestBody ObjectNode payload){
        String fechaInicio = payload.get("fecha_ini").asText();
        String fechaFin = payload.get("fecha_fin").asText();
        int idOficina = payload.get("idOf").asInt();

        ObjectNode s = service.paquetesXoficinaXfecha_linea(idOficina, fechaInicio, fechaFin);
        return ResponseEntity.ok(s);
    }
    
    @PostMapping("/graficos/paquetesXvuelosXfecha")
    public ResponseEntity<?> paquetesXvuelosXfecha(@RequestBody RangoFechas rf){
        ObjectNode s = service.paquetesXvuelosXfecha(rf.fecha_ini,rf.fecha_fin);
        return ResponseEntity.ok(s);
    }
    
    @PostMapping("/graficos/paquetesXoficinasXfecha_barra")
    public ResponseEntity<?> paquetesXoficinasXfecha_barra(@RequestBody RangoFechas rf){
        ObjectNode s = service.paquetesXoficinasXfecha_barra(rf.fecha_ini,rf.fecha_fin);
        return ResponseEntity.ok(s);
    }
}
