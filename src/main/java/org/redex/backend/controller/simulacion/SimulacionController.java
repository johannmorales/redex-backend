package org.redex.backend.controller.simulacion;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.controller.simulacion.simulador.Simulador;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.security.CurrentUser;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.exception.MyFileNotFoundException;
import org.redex.backend.zelper.response.ApplicationResponse;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.albatross.zelpers.miscelanea.JsonHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("simulacion")
public class SimulacionController {

    public static Logger logger = LogManager.getLogger(SimulacionController.class);

    @Autowired
    SimulacionService service;

    @Autowired
    Simulador simulador;

    @RequestMapping("/estado")
    public void greeting(@RequestParam Long id) {

    }

    @GetMapping("crear")
    public ResponseEntity<?> crear() {
        service.crear();
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion creada"));
    }

    @GetMapping("eliminar")
    public ResponseEntity<?> borrar() {
        simulador.eliminar();
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion eliminada"));
    }

    @GetMapping("resetear")
    public ResponseEntity<?> resetear() {
        simulador.resetear();
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion reseteada"));
    }

    @PostMapping("/paquetes/carga")
    public CargaDatosResponse cargaPaquetes(@RequestParam("file") MultipartFile file) {
        return service.cargaPaquetes(file);
    }

    @PostMapping("/vuelos/carga")
    public CargaDatosResponse cargaVuelos(@RequestParam("file") MultipartFile file) {
        return service.cargaVuelos(file);
    }

    @PostMapping("/oficinas/carga")
    public CargaDatosResponse cargaOficinas( @RequestParam("file") MultipartFile file) {
        return service.cargaOficinas(file);
    }

    @GetMapping("oficinas")
    public ArrayNode oficinas() {
        Map<String, Oficina> oficinas = simulador.getOficinas();
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Map.Entry<String, Oficina> entry : oficinas.entrySet()) {
            Oficina oficina = entry.getValue();
            ObjectNode oficinaNode = JsonHelper.createJson(oficina, JsonNodeFactory.instance, new String[]{
                    "id",
                    "codigo",
                    "capacidadActual",
                    "capacidadMaxima",
                    "pais.id",
                    "pais.codigo",
                    "pais.codigoIso",
                    "pais.latitud",
                    "pais.longitud"
            });
            arr.add(oficinaNode);
        }
        
        return arr;
    }

    @GetMapping("vuelos")
    public ArrayNode vuelos() {
        List<Vuelo> vuelos = simulador.getVuelos();
        Map<String, Oficina> oficinas = simulador.getOficinas();
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Map.Entry<String, Oficina> entry : oficinas.entrySet()) {
            Oficina oficina = entry.getValue();
            ObjectNode oficinaNode = JsonHelper.createJson(oficina, JsonNodeFactory.instance, new String[]{
                    "id",
                    "codigo",
                    "capacidadActual",
                    "capacidadMaxima",
                    "pais.id",
                    "pais.codigo",
                    "pais.codigoIso",
                    "pais.latitud",
                    "pais.longitud"
            });
            arr.add(oficinaNode);
        }
        return arr;
    }


    @PostMapping("window")
    public ResponseEntity<?> getWindow(@RequestBody Ventana v) {
        System.out.println(v.getInicio());
        System.out.println(v.getFin());
        List<SimulacionAccionWrapper> acciones = simulador.procesarVentana(v);
        
        int termino = simulador.getTermino();
        
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        System.out.println("Termino: "+ termino);
        Termino t = new Termino();
        t.setStatus(termino);
        arr.add(JsonHelper.createJson(t,JsonNodeFactory.instance));
        for (SimulacionAccionWrapper accion : acciones) {
            arr.add(JsonHelper.createJson(accion, JsonNodeFactory.instance));
        }

        return ResponseEntity.ok(arr);
    }

    @PostMapping("reporte")
    public ResponseEntity<Resource> reporte(@RequestBody SimulacionReporte payload) {
        String archivo = service.reporte(payload);
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

            String actualFileName = archivo.substring(archivo.lastIndexOf('/') + 1);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, actualFileName)
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado ", ex);
        }
    }

}
