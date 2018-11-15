/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.reportes;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.controller.archivos.ArchivosController;
import org.redex.backend.controller.personas.PersonasService;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.Archivo;
import org.redex.backend.model.general.Persona;
import org.redex.backend.zelper.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.albatross.zelpers.miscelanea.JsonHelper;

/**
 *
 * @author Oscar
 */
public class ReportesController {
    
    private static final Logger logger = LogManager.getLogger(ReportesController.class);
    
    @Autowired
    ReportesService service;
    
    @PostMapping("/reportes/paquetesXvuelo")
    public ResponseEntity<Resource> paquetesXvuelo(@RequestBody VueloAgendado va){
        String archivo = service.paquetesXvuelo(va.getId());
        try {
          
           
           Resource resource = new UrlResource(archivo);
           
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }
            
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo+ "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado " , ex);
        }
    }
    
    @PostMapping("/reportes/paquetesXusuario")
    public ResponseEntity<Resource> paquetesXusuario(@RequestBody Persona p){
        String archivo = service.paquetesXusuario(p.getId());
        try {

           
           Resource resource = new UrlResource(archivo);
           
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }
            
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo+ "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado " , ex);
        }
    }
    
    @PostMapping("/reportes/enviosXfechas")
    public ResponseEntity<Resource> enviosXfechas(@RequestBody RangoFechas r){
        String archivo = service.enviosXfechas(r.fecha_ini,r.fecha_fin);
        try {
            
           
           Resource resource = new UrlResource(archivo);
           
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }
            
            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo+ "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado " , ex);
        }
    }
}
