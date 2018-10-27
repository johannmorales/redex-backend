package org.redex.backend.controller.planvuelo;

import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PlanVueloService {

    public CargaDatosResponse carga(MultipartFile file);

    void guardar(PlanVuelo planVuelo);

    void editarVuelo(Vuelo vuelo);

    void actualizarVuelo(Vuelo vuelo);

    public PlanVuelo findActivo();

    public void desactivarVuelo(Long id);

    public void activarVuelo(Long id);

    public Page<Vuelo> allByCrimson(CrimsonTableRequest request);
}
