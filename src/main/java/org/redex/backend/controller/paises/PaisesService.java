package org.redex.backend.controller.paises;

import java.util.List;
import org.redex.backend.model.general.Pais;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;

public interface PaisesService {

    public List<Pais> all();

    public List<Pais> allByNombre(String nombre);

    public Page<Pais> crimsonList(CrimsonTableRequest request);

    public Pais find(Long id);

    public void save(Pais p);
    
}
