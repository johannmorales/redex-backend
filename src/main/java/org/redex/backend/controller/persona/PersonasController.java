package org.redex.backend.controller.persona;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
    public CargaDatosResponse carga(@RequestParam("file") MultipartFile file) {
        return service.carga(file);
    }

    @GetMapping("/{id}")
    public ObjectNode find(@PathVariable Long id) {
        Persona item = service.find(id);

        return JsonHelper.createJson(item, JsonNodeFactory.instance, new String[]{
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
        });
    }

}