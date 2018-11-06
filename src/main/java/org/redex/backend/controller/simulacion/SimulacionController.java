package org.redex.backend.controller.simulacion;

import java.time.LocalDateTime;
import java.util.List;

import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVuelo;
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
        Simulacion s = simulacionRepository.getOne(request.getSimulacion());
        LocalDateTime inicio = request.getInicio();
        LocalDateTime fin = request.getFin();
        List<SimulacionAccion> acciones = service.accionesByWindow(request);

        List<SimulacionPaquete> paquetes = paquetesRepository.findAllBySimulacionAndFechaIngresoBetween(s, inicio, fin);

        return ResponseEntity.ok(acciones);
    }

}
