package org.redex.backend.controller.reportes;

import org.redex.backend.security.DataSession;

import java.time.LocalDate;

public interface ReportesService {

    String paquetesXvuelo(Long id, DataSession ds);
    
    String enviosXfechas(LocalDate inicio, LocalDate fin, DataSession ds);
    
    String paquetesXusuario(Long id, DataSession ds);
    
    String enviosXoficina(LocalDate inicio, LocalDate fin, DataSession ds);

    String enviosFinalizados(LocalDate inicio, LocalDate fin, DataSession ds);

    String auditoria(LocalDate inicio, LocalDate fin, Long idOficina, DataSession ds);
}
