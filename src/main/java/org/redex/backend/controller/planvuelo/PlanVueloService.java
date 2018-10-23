package org.redex.backend.controller.planvuelo;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.envios.PlanVuelo;
import org.redex.model.envios.Vuelo;
import org.redex.model.general.Archivo;

public interface PlanVueloService {

    public CargaDatosResponse carga(Archivo archivo);

    void guardar(PlanVuelo planVuelo);

    void editarVuelo(Vuelo vuelo);

    void desactivarVuelo(Vuelo vuelo);

    void activarVuelo(Vuelo vuelo);

    void actualizarVuelo(Vuelo vuelo);
}
