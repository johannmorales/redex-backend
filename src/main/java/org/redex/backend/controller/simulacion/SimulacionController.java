package org.redex.backend.controller.simulacion;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.redex.backend.controller.simulacionaccion.SimulacionAccionWrapper;
import org.redex.backend.model.envios.Paquete;
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
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.albatross.zelpers.miscelanea.JsonHelper;
import pe.albatross.zelpers.miscelanea.ObjectUtil;

@RestController
@RequestMapping("simulaciones")
public class SimulacionController {

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

    @RequestMapping("/{id}/estado")
    public void greeting(@RequestParam Long id) {

    }

    @PostMapping()
    public ResponseEntity<?>  crear() {
        Simulacion s = service.crear();
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion creada", s));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApplicationResponse.of("Simulacion eliminada"));
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
        List<SimulacionOficina> oficinas = service.listOficinas(id);
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (SimulacionOficina oficina : oficinas) {

            ObjectNode oficinaNode = JsonHelper.createJson(oficina, JsonNodeFactory.instance, new String[]{
                    "id",
                    "capacidadMaxima",
                    "pais.id",
                    "pais.codigo",
                    "pais.codigoIso",
                    "pais.latitud",
                    "pais.longitud",
            });
            oficinaNode.put("capacidadActual",oficina.getCapacidadInicial());

            arr.add(oficinaNode);
        }

        return arr;
    }


    @PostMapping("/{id}/oficinas/carga")
    public CargaDatosResponse cargaOficinas(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return service.cargaOficinas(id, file);
    }
    
    @GetMapping
    public CrimsonTableResponse crimsonList(CrimsonTableRequest request){
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
    public ResponseEntity<?> getWindow(@RequestBody WindowRequest request){
        List<SimulacionAccion> acciones = service.accionesByWindow(request);
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (SimulacionAccion accion : acciones) {
            arr.add(JsonHelper.createJson(SimulacionAccionWrapper.of(accion), JsonNodeFactory.instance));
        }
        return ResponseEntity.ok(arr);
    }

}
