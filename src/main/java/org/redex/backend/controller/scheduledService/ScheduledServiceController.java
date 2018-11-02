/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.scheduledService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author Oscar
 */
public class ScheduledServiceController {
    
    @Autowired
    ScheduledServiceService service;
    
    @Scheduled(fixedRate=10)
    public void despachoVuelo(){
        
        service.salidaVuelos();
        service.llegadaVuelos();
        System.out.println("hola");
    }
}
