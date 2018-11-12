package org.redex.backend.controller.personas;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.Valid;

import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Persona;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public CrimsonTableResponse crimsonList(@Valid CrimsonTableRequest request) {
        Page<Persona> list = service.allByCrimson(request);
        return CrimsonTableResponse.of(list, new String[]{
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

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody PersonaRegistro form) {
        Persona persona = service.save(form);
        return ResponseEntity.ok(JsonHelper.createJson(persona, JsonNodeFactory.instance, new String[]{
                "id",
                "numeroDocumentoIdentidad",
                "nombreCompleto",
                "nombreCorto",
                "tipoDocumentoIdentidad.id",
                "tipoDocumentoIdentidad.simbolo"
        }));
    }
    
    @PostMapping("/editar")
    public ResponseEntity<?> editar(@RequestBody Persona persona){
        service.editar(persona);
        return ResponseEntity.ok("Persona actualizada");
    }

    @PostMapping("/find")
    public ResponseEntity<?> find(@RequestBody FindPersonaRequest request) {
        Persona persona = service.findByDocumento(request.tipoDocumentoIdentidad, request.numeroDocumentoIdentidad);

        if (persona != null) {
            return ResponseEntity.ok(JsonHelper.createJson(persona, JsonNodeFactory.instance, new String[]{
                "id",
                "nombreCompleto",
                "nombreCorto"
            }));
        } else {
            throw new ResourceNotFoundException("Persona", "DNI", request.numeroDocumentoIdentidad);
        }

    }
}
