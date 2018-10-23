package org.redex.backend.controller.usuarios;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    UsuariosService usuariosService;

    @GetMapping
    public ArrayNode list() {
        
    }

    @PostMapping("/carga")
    public CargaDatosResponse carga(@RequestBody Archivo archivo) {
        return usuariosService.carga(archivo);
    }
}
