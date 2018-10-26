package org.redex.backend.controller.paquetes;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("/paquetes")
public class PaquetesController {

    @Autowired
    PaquetesService service;

    @GetMapping
    public ArrayNode list() {
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);

        List<Paquete> list = service.list();

        for (Paquete item : list) {
            arr.add(JsonHelper.createJson(item, JsonNodeFactory.instance, new String[]{
                "id",
                "personaOrigen.id",
                "personaOrigen.nombreCompleto",
                "personaOrigen.numeroDocumentoIdentidad",
                "personaOrigen.tipoDocumentoIdentidad.id",
                "personaOrigen.tipoDocumentoIdentidad.simbolo",
                "personaDestino.id",
                "personaDestino.nombreCompleto",
                "personaDestino.numeroDocumentoIdentidad",
                "personaDestino.tipoDocumentoIdentidad.id",
                "personaDestino.tipoDocumentoIdentidad.simbolo",
                "oficinaOrigen.id",
                "oficinaOrigen.codigo",
                "oficinaOrigen.pais.id",
                "oficinaOrigen.pais.nombre",
                "oficinaDestino.id",
                "oficinaDestino.codigo",
                "oficinaDestino.pais.id",
                "oficinaDestino.pais.nombre",
                "codigoRastreo"
            }));
        }

        return arr;
    }
}
