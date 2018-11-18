package org.redex.backend.controller.graficos;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.redex.backend.model.dashboard.PaquetesVuelo;

import java.time.LocalDate;
import java.util.List;

public interface GraficosService {
    
    ObjectNode paquetesXoficinaXfecha_linea(int id,String fechaI,String fechaF);
    
    List<PaquetesVuelo> paquetesXvuelosXfecha(LocalDate inicio, LocalDate fin);
    
    ObjectNode paquetesXoficinasXfecha_barra(String fechaI,String fechaF);
    
}
