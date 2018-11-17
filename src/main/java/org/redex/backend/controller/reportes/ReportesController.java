package org.redex.backend.controller.reportes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.VueloAgendado;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class ReportesController {

    private static final Logger logger = LogManager.getLogger(ReportesController.class);

    @Autowired
    ReportesService service;

    @PostMapping("/reportes/paquetesXvuelo")
    public ResponseEntity<Resource> paquetesXvuelo(@RequestBody VueloAgendado va) {
        String archivo = service.paquetesXvuelo(va.getId());
        return download(archivo);

    }

    @PostMapping("/reportes/paquetesXusuario")
    public ResponseEntity<Resource> paquetesXusuario(@RequestBody Persona p) {
        String archivo = service.paquetesXusuario(p.getId());
        return download(archivo);

    }

    @PostMapping("/reportes/enviosXfechas")
    public ResponseEntity<Resource> enviosXfechas(@RequestBody RangoFechas r) {
        String archivo = service.enviosXfechas(r.fecha_ini, r.fecha_fin);
        return download(archivo);
    }

    private ResponseEntity<Resource> download(String archivo) {
        try {

            Resource resource = new UrlResource("file",archivo);
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }

            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo + "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado ", ex);
        }
    }

}
