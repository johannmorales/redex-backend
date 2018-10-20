package org.redex.backend.controller.oficinas;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oficinas")
public class OficinasController {

    @Autowired
    OficinasService oficinasService;
    
    @PostMapping("/carga")
    public CargaDatosResponse carga(Archivo archivo) {
        return oficinasService.carga(archivo);
    }
}
