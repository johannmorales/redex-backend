package org.redex.backend.controller.scheduledService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
<<<<<<< HEAD
=======

>>>>>>> da85ec90504cfb6735f832f93d38305be33f004b
@Component
public class ScheduledServiceController {
    
    @Autowired
    ScheduledServiceService service;
    
<<<<<<< HEAD
    @Scheduled(fixedRate=60000)
=======
    @Scheduled(fixedRate=10000)
>>>>>>> da85ec90504cfb6735f832f93d38305be33f004b
    public void despachoVuelo(){
        
        //service.llegadaVuelos();
        //generarRutas();
        //service.salidaVuelos();
        
        System.out.println("hola");
    }
}
