package org.redex.backend.controller.planvuelo;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class PlanVueloController {
    
    @Autowired
    PlanVueloService planVueloService;
    
    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestBody Archivo archivo) {
        return planVueloService.cargar(archivo);
    }
}
