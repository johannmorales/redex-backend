package org.redex.backend.controller.oficinas;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.Valid;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/oficinas")
public class OficinasController {

    @Autowired
    OficinasService service;

    @GetMapping
    public CrimsonTableResponse crimsonList(@Valid CrimsonTableRequest request) {
        Page<Oficina> oficinas = service.allByCrimson(request);
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

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestParam("file") MultipartFile file) {
        return service.carga(file);
    }

    @GetMapping("/{id}")
    public ObjectNode find(@PathVariable Long id) {
        Oficina oficina = service.find(id);

        return JsonHelper.createJson(oficina, JsonNodeFactory.instance, new String[]{
            "id",
            "pais.id",
            "pais.codigo",
            "pais.nombre",
            "capacidadActual",
            "capacidadMaxima",
            "codigo"
        });
    }

    @PostMapping("{id}/desactivar")
    public void desactivar(@PathVariable Long id) {
        service.desactivar(id);
    }

    @PostMapping("{id}/activar")
    public void acrivar(@PathVariable Long id) {
        service.activar(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody Oficina oficina) {
        if (oficina.getId() != null) {
            service.save(oficina);
        } else {
            service.update(oficina);
        }
    }

}
