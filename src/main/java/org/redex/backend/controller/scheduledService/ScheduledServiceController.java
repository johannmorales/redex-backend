package org.redex.backend.controller.scheduledService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledServiceController {

    private static Logger logger = LogManager.getLogger(ScheduledServiceController.class);

    @Autowired
    ScheduledServiceService service;

    @Scheduled(cron = "3 * * * * *")
    public void despachoVuelo() {
        logger.info("Cron inicia...");
        service.llegadaVuelos();
        service.generarRutas();
        service.salidaVuelos();
        logger.info("Cron terminado");
    }
}