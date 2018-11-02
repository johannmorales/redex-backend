package org.redex.backend.controller.simulacion;

import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.redex.backend.zelper.response.ApplicationResponse;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("simulaciones")
public class SimulacionController {

    @Autowired
    SimulacionService service;

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
    
    @GetMapping("window")
    public ResponseEntity<?> getWindow(WindowRequest request){
        List<SimulacionAccion> acciones = service.accionesByWindow(request);
        return ResponseEntity.ok(acciones);
    }

}
