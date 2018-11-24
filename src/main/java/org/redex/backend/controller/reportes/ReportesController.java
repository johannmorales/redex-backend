package org.redex.backend.controller.reportes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.Persona;
import org.redex.backend.zelper.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.time.LocalDate;

@RestController
public class ReportesController {

    private final Logger logger = LogManager.getLogger(ReportesController.class);

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

    @GetMapping("/reportes/enviosXfechas")
    public ResponseEntity<Resource> enviosXfechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosXfechas(inicio, fin);
        return download(archivo);
    }

    @GetMapping("/reportes/enviosXoficina")
    public ResponseEntity<Resource> enviosXoficina(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosXoficina(inicio, fin);
        return download(archivo);
    }

    @GetMapping("/reportes/enviosFinalizados")
    public ResponseEntity<Resource> enviosFinalizados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosFinalizados(inicio, fin);
        return download(archivo);
    }


    private ResponseEntity<Resource> download(String archivo) {
        logger.info("REPORTE {}", archivo);
        try {

            Resource resource = new UrlResource("file", archivo);
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }

            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, archivo)
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado ", ex);
        }
    }


    // reporte de envios para cada oficina mensual johana manda las fechas

    // reporte de envios finalizados en rango de fecha

}
