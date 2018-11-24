package org.redex.backend.controller.reportes;

import java.time.LocalDate;

public interface ReportesService {

    public String paquetesXvuelo(Long id);
    
    public String enviosXfechas(LocalDate inicio, LocalDate fin);
    
    public String paquetesXusuario(Long id);
    
    public String accionesXusuarioXoficinaXfecha();

    String enviosXoficina(LocalDate inicio, LocalDate fin);

    String enviosFinalizados(LocalDate inicio, LocalDate fin);
}
