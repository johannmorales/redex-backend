package org.redex.backend.controller.persona;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.general.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("personas")
public class PersonasController {

    @Autowired
    PersonasService service;

    @GetMapping
    public ArrayNode list() {
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        List<Persona> list = service.all();
        for (Persona item : list) {
            arr.add(JsonHelper.createJson(item, JsonNodeFactory.instance, new String[]{
                "id",
                "nombres",
                "paterno",
                "materno",
                "email",
                "telefono",
                "celular",
                "pais.id",
                "tipoDocumentoIdentidad.*",
                "numeroDocumentoIdentidad",
                "pais.codigo",
                "pais.nombre"
            }));
        }
        return arr;
    }
    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestBody Archivo archivo) {
        return service.carga(archivo);
    }

}
