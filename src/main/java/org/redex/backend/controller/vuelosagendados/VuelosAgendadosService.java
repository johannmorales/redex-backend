package org.redex.backend.controller.vuelosagendados;

import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;

public interface VuelosAgendadosService {

    public Page<VueloAgendado> crimsonList(CrimsonTableRequest request, DataSession ds);

    public Integer generar(GenerarPayload payload);

    void eliminarInnecesarios();
}
