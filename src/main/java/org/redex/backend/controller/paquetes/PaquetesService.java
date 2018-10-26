package org.redex.backend.controller.paquetes;

import java.util.List;
import org.redex.backend.model.envios.Paquete;

public interface PaquetesService {

    public List<Paquete> list();

    public Paquete find(Long id);

}
