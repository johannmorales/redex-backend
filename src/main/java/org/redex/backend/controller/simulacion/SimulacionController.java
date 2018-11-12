package org.redex.backend.controller.simulacion;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.simulacion.*;
import org.redex.backend.repository.SimulacionPaquetesRepository;
import org.redex.backend.repository.SimulacionRepository;
import org.redex.backend.repository.SimulacionVueloAgendadoRepository;
import org.redex.backend.repository.SimulacionVuelosRepository;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.redex.backend.zelper.response.ApplicationResponse;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("simulaciones")
public class SimulacionController {

    public static Logger logger = LogManager.getLogger(SimulacionController.class);
    @Autowired
    SimulacionService service;

    @Autowired
    SimulacionRepository simulacionRepository;

    @Autowired
    SimulacionPaquetesRepository paquetesRepository;

    @Autowired
    SimulacionVueloAgendadoRepository vueloAgendadoRepository;

    @Autowired
    SimulacionVuelosRepository vuelosRepository;

    @Autowired
    VisorSimulacion visorSimulacion;

    @RequestMapping("/{id}/estado")
    public void greeting(@RequestParam Long id) {

    }

    @PostMapping()
    public ResponseEntity<?> crear() {
        Simulacion s = service.crear();
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion creada", s));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion eliminada"));
    }

    @GetMapping("/{id}/resetear")
    public ResponseEntity<?> resetear(@PathVariable Long id) {
        service.resetear(id);
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion reseteada"));
    }

    @PostMapping("/{id}/paquetes/carga")
    public CargaDatosResponse cargaPaquetes(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return service.cargaPaquetes(id, file);
    }

    @PostMapping("/{id}/vuelos/carga")
    public CargaDatosResponse cargaVuelos(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return service.cargaVuelos(id, file);
    }

    @GetMapping("/{id}/oficinas")
    public ArrayNode oficinas(@PathVariable Long id) {
        Map<String, AlgoritmoOficina> oficinas = visorSimulacion.getOficinas();
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (Map.Entry<String, AlgoritmoOficina> entry : oficinas.entrySet()) {
            AlgoritmoOficina oficina = entry.getValue();
            ObjectNode oficinaNode = JsonHelper.createJson(oficina, JsonNodeFactory.instance, new String[]{
                    "id",
                    "codigo",
                    "capacidadMaxima",
                    "pais.id",
                    "pais.codigo",
                    "pais.codigoIso",
                    "pais.latitud",
                    "pais.longitud"
            });
            oficinaNode.put("capacidadActual", oficina.getCapacidadActual());
            arr.add(oficinaNode);
        }

        return arr;
    }

    @PostMapping("/{id}/oficinas/carga")
    public CargaDatosResponse cargaOficinas(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return service.cargaOficinas(id, file);
    }

    @GetMapping
    public CrimsonTableResponse crimsonList(CrimsonTableRequest request) {
        Page<Simulacion> list = service.crimsonList(request);

        return CrimsonTableResponse.of(list, new String[]{
                "id",
                "estado",
                "cantidadPaquetes",
                "cantidadPaquetesEntregados",
                "cantidadOficinas",
                "cantidadVuelos"
        });
    }

    @PostMapping("window")
    public ResponseEntity<?> getWindow(@RequestBody WindowRequest request) {
        Ventana v = new Ventana();

        v.setInicio(request.getInicio());
        v.setFin(request.getFin());

        List<SimulacionAccionWrapper> acciones = visorSimulacion.procesarVentana(v);

        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (SimulacionAccionWrapper accion : acciones) {
            arr.add(JsonHelper.createJson(accion, JsonNodeFactory.instance));
        }

        return ResponseEntity.ok(arr);
    }

}
