package org.redex.backend.controller.johana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JohanaController {

    @Autowired
    JohanaService service;
    
    @PostMapping(value = "/accion")
    public void accion(@RequestBody ExamplePayload example) {
        service.realizarAccion(example);
    }

}
