package org.redex.backend.controller.paises;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.model.general.Pais;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("paises")
public class PaisesController {

    @Autowired
    PaisesService service;

    @GetMapping
    public CrimsonTableResponse crimsonList(CrimsonTableRequest request) {
        Page<Pais> list = service.crimsonList(request);
        return CrimsonTableResponse.of(list, new String[]{
            "*",
            "continente.id",
            "continente.nombre"
        });
    }

    @GetMapping("search")
    public ArrayNode search(@RequestParam String q) {
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        List<Pais> list = service.allByNombre(q);
        for (Pais item : list) {
            arr.add(JsonHelper.createJson(item, JsonNodeFactory.instance, new String[]{
                "*",
                "continente.id",
                "continente.nombre"
            }));
        }
        return arr;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        Pais pais = service.find(id);

        return ResponseEntity.ok(JsonHelper.createJson(pais, JsonNodeFactory.instance, new String[]{
            "*",
            "continente.id",
            "continente.nombre"
        }));
    }

}
