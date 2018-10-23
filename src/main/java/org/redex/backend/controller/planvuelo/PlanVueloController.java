package org.redex.backend.controller.planvuelo;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.envios.PlanVuelo;
import org.redex.model.general.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("/vuelos")
public class PlanVueloController {

    @Autowired
    PlanVueloService planVueloService;

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestBody Archivo archivo) {
        return planVueloService.carga(archivo);
    }

    @GetMapping
    public ObjectNode list() {
        PlanVuelo pv = planVueloService.findActivo();

        return JsonHelper.createJson(pv, JsonNodeFactory.instance, new String[]{
            "id",
            "vuelos.id",
            "vuelos.oficinaOrigen.id",
            "vuelos.oficinaOrigen.codigo",
            "vuelos.oficinaOrigen.pais.id",
            "vuelos.oficinaOrigen.pais.nombre",
            "vuelos.oficinaDestino.id",
            "vuelos.oficinaDestino.pais.id",
            "vuelos.oficinaDestino.pais.nombre",
            "vuelos.oficinaDestino.codigo",
            "vuelos.horaInicioString",
            "vuelos.horaFinString",
            "vuelos.estado",
            "vuelos.capacidad"

        });
    }
    
    @PostMapping("/vuelos/{id}/desactivar")
    public void desactivarVuelo(@PathVariable Long id){
        planVueloService.desactivarVuelo(id);
    }

    @PostMapping("/vuelos/{id}/activar")
    public void activarVuelo(@PathVariable Long id){
        planVueloService.activarVuelo(id);
    }

}
