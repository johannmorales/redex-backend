package org.redex.backend.controller.vuelos;

import org.redex.backend.controller.planvuelo.VueloWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vuelos")
public class VuelosController {

    @Autowired
    VuelosService service;

    @GetMapping("{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        return ResponseEntity.ok(VueloWrapper.of(service.find(id)));
    }

    @PostMapping("save")
    public ResponseEntity<?> save(VueloWrapper wrapper) {
        if (wrapper.id != null) {
            return ResponseEntity.ok(VueloWrapper.of(service.update(wrapper)));
        } else {
            return ResponseEntity.ok(VueloWrapper.of(service.save(wrapper)));
        }
    }
}
