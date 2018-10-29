package org.redex.backend.controller.simulacionaccion;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("simulacion")
public class SimulacionAccionController {
    
    @Autowired
    SimulacionAccionService service;
    
    @GetMapping("/{id}/acciones/all")
    public ResponseEntity<?> list(@PathVariable Long id){
        List<SimulacionAccion> list = service.list(id);
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        for (SimulacionAccion item : list) {
            arr.add(JsonHelper.createJson(SimulacionAccionWrapper.of(item), JsonNodeFactory.instance));
        }
        return ResponseEntity.ok(arr);
    }
}
