package org.redex.backend.controller.planvuelo;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.Valid;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("/planvuelo")
public class PlanVueloController {

    @Autowired
    PlanVueloService service;

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestParam("file") MultipartFile file) {
        return service.carga(file);
    }

    @GetMapping
    public CrimsonTableResponse crimsonList(@Valid CrimsonTableRequest request) {
        Page<Vuelo> oficinas = service.allByCrimson(request);
        return CrimsonTableResponse.of(oficinas, new String[]{
            "id",
            "pais.id",
            "pais.codigo",
            "pais.nombre",
            "capacidadActual",
            "capacidadMaxima",
            "estado",
            "codigo"
        });
    }

    @GetMapping("all")
    public ObjectNode all() {
        PlanVuelo pv = service.findActivo();

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
            "vuelos.capacidad",
            "vuelos.duracion"

        });
    }

    @PostMapping("/vuelos/{id}/desactivar")
    public void desactivarVuelo(@PathVariable Long id) {
        service.desactivarVuelo(id);
    }

    @PostMapping("/vuelos/{id}/activar")
    public void activarVuelo(@PathVariable Long id) {
        service.activarVuelo(id);
    }

}
