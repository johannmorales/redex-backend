package org.redex.backend.controller.tipodocidentidad;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.model.general.TipoDocumentoIdentidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("tipodocidentidad")
public class TipoDocIdentidadController {
    
    @Autowired
    TipoDocIdentidadService service;
    
     @GetMapping
    public ArrayNode list() {
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        List<TipoDocumentoIdentidad> list = service.all();
        for (TipoDocumentoIdentidad item : list) {
            arr.add(JsonHelper.createJson(item, JsonNodeFactory.instance, new String[]{
                "*",
                "pais.id",
                "pais.codigo",
                "pais.nombre",
            }));
        }
        return arr;
    }
}
