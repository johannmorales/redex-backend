package org.redex.backend.controller.scheduledService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledServiceController {
    
    @Autowired
    ScheduledServiceService service;
    
    @Scheduled(fixedRate=10000)
    public void despachoVuelo(){
        
        //service.llegadaVuelos();
        //generarRutas();
        //service.salidaVuelos();
        
        System.out.println("hola");
    }
}
