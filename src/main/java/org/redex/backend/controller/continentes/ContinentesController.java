package org.redex.backend.controller.continentes;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.model.general.Continente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("continentes")
public class ContinentesController {
    
    @Autowired
    ContinentesService service;
    
    @GetMapping
    public ResponseEntity<?> all(){
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        
        List<Continente> items = service.all();
        for (Continente item : items) {
            arr.add(JsonHelper.createJson(item, JsonNodeFactory.instance));
        }
        
        return ResponseEntity.ok(arr);
    }
}
