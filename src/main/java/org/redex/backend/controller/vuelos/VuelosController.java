package org.redex.backend.controller.vuelos;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.redex.backend.controller.planvuelo.VueloWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("vuelos")
public class VuelosController {

    @Autowired
    VuelosService service;

    @GetMapping("{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        
        return ResponseEntity.ok(JsonHelper.createJson(service.find(id), JsonNodeFactory.instance, new String[] {
            "id",
            "planVuelo.id",
            "oficinaOrigen.id",
            "oficinaOrigen.codigo",
            "oficinaOrigen.pais.id",
            "oficinaOrigen.pais.nombre",
            "oficinaDestino.id",
            "oficinaDestino.codigo",
            "oficinaDestino.pais.id",
            "oficinaDestino.pais.nombre",
            "horaInicioString",
            "horaFinString",
            "capacidad"
        }));
    }

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Vuelo vuelo) {
        if (vuelo.getId() != null) {
            return ResponseEntity.ok(VueloWrapper.of(service.update(vuelo)));
        } else {
            return ResponseEntity.ok(VueloWrapper.of(service.save(vuelo)));
        }
    }
}