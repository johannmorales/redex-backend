package org.redex.backend.controller.usuarios;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.Valid;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.seguridad.Usuario;
import org.redex.backend.security.CurrentUser;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.crimsontable.CrimsonTableResponse;
import org.redex.backend.zelper.response.ApplicationResponse;
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
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    UsuariosService service;

    @GetMapping
    public CrimsonTableResponse list(@Valid CrimsonTableRequest request) {
        Page<Usuario> usuarios = service.crimsonList(request);
        return CrimsonTableResponse.of(usuarios, new String[]{
            "id",
            "username",
            "estado",
            "colaborador.id",
            "colaborador.persona.id",
            "colaborador.persona.nombreCompleto",
            "colaborador.persona.email",
            "colaborador.persona.numeroDocumentoIdentidad",
            "colaborador.persona.tipoDocumentoIdentidad.id",
            "colaborador.persona.tipoDocumentoIdentidad.simbolo",
            "colaborador.persona.tipoDocumentoIdentidad.nombre",
            "colaborador.email",
            "colaborador.oficina.pais.id",
            "colaborador.oficina.pais.nombre",
            "colaborador.oficina.codigo",
            "rol.id",
            "rol.nombre"
        });
    }

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestParam("file") MultipartFile file) {
        return service.carga(file);
    }

    @PostMapping("{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.ok(ApplicationResponse.of("Usuario desactivado"));
    }

    @PostMapping("{id}/activar")
    public ResponseEntity<?> acrivar(@PathVariable Long id) {
        service.activar(id);
        return ResponseEntity.ok(ApplicationResponse.of("Usuario activado"));
    }

    // johana!!
    // el metodo save lo llamas con localhost:5000/usuarios/save
    @PostMapping("/save")
    public void carga(@RequestBody UsuariosPayload payload) {
        service.crearUsuario(payload);
    }

    @GetMapping("/yo")
    public ObjectNode yo(@CurrentUser DataSession ds) {
        return JsonHelper.createJson(ds, JsonNodeFactory.instance, new String[]{
            "colaborador.oficina.codigo",
            "persona.nombreCorto",
            "username",
            "rol.id",
            "rol.nombre",
            "rol.codigo"
        });
    }
}
