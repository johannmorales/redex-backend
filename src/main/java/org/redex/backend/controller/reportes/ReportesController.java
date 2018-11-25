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

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;

@RestController
@RequestMapping("reportes")
public class ReportesController {

    @Autowired
    ReportesService service;

    @GetMapping("paquetesXvuelo")
    public ResponseEntity<Resource> paquetesXvuelo(@RequestParam Long idVueloAgendado) {
        String archivo = service.paquetesXvuelo(idVueloAgendado);
        return download(archivo);

    }

    @GetMapping("paquetesXusuario")
    public ResponseEntity<Resource> paquetesXusuario(@RequestParam Long idUsuario) {
        String archivo = service.paquetesXusuario(idUsuario);
        return download(archivo);

    }

    @GetMapping("enviosXfechas")
    public ResponseEntity<Resource> enviosXfechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosXfechas(inicio, fin);
        return download(archivo);
    }

    @GetMapping("enviosXoficina")
    public ResponseEntity<Resource> enviosXoficina(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosXoficina(inicio, fin);
        return download(archivo);
    }

    @GetMapping("enviosFinalizados")
    public ResponseEntity<Resource> enviosFinalizados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        String archivo = service.enviosFinalizados(inicio, fin);
        return download(archivo);
    }


    private ResponseEntity<Resource> download(String archivo) {
        try {
            Resource resource = new UrlResource("file", archivo);
            try {
                System.out.println(resource.getFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo);
            }

            String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            String actualFileName = archivo.substring(archivo.lastIndexOf('/')+1);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, actualFileName)
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado ", ex);
        }
    }


    // reporte de envios para cada oficina mensual johana manda las fechas

    // reporte de envios finalizados en rango de fecha

}
