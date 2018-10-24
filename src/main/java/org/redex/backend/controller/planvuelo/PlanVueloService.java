package org.redex.backend.controller.planvuelo;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.general.Archivo;

public interface PlanVueloService {

    public CargaDatosResponse carga(Archivo archivo);

    void guardar(PlanVuelo planVuelo);

    void editarVuelo(Vuelo vuelo);

    void actualizarVuelo(Vuelo vuelo);

    public PlanVuelo findActivo();

    public void desactivarVuelo(Long id);

    public void activarVuelo(Long id);
}
