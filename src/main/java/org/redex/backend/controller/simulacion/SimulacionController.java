package org.redex.backend.controller.simulacion;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("simulacion")
public class SimulacionController {
    
    
    @RequestMapping("/{id}/estado")
    public void greeting(@RequestParam Long id) {
        
    }
    
}
