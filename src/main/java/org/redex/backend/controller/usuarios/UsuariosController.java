package org.redex.backend.controller.usuarios;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Archivo;
import org.redex.backend.model.seguridad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    UsuariosService usuariosService;

    @GetMapping
    public ArrayNode list() {
        ArrayNode arr = new ArrayNode(JsonNodeFactory.instance);
        List<Usuario> usuarios = usuariosService.all();
        for (Usuario usuario : usuarios) {
            arr.add(JsonHelper.createJson(usuario, JsonNodeFactory.instance, new String[]{
                "id",
                "username",
                "estado",
                "colaborador.id",
                "colaborador.persona.id",
                "colaborador.persona.nombres",
                "colaborador.persona.paterno",
                "colaborador.persona.email",
                "colaborador.persona.numeroDocumentoIdentidad",
                "colaborador.persona.tipoDocumentoIdentidad.id",
                "colaborador.persona.tipoDocumentoIdentidad.simbolo",
                "colaborador.persona.tipoDocumentoIdentidad.nombre",
                "colaborador.email",
                "colaborador.oficina.pais.id",
                "colaborador.oficina.pais.nombre",
                "colaborador.oficina.codigo"
            }));
        }
        return arr;
    }

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestBody Archivo archivo) {
        return usuariosService.carga(archivo);
    }

    
    // johana!!
    // el metodo save lo llamas con localhost:5000/usuarios/save
    @PostMapping("/save")
    public void carga(@RequestBody UsuariosPayload payload) {
        usuariosService.crearUsuario(payload);
    }
}
