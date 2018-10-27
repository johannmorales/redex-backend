package org.redex.backend.controller.roles;

import java.util.List;
import org.redex.backend.model.seguridad.Rol;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;

public interface RolesService {

    public Page<Rol> crimsonList(CrimsonTableRequest request);
    
    public void find(Long id);
    
    public void save(Rol rol);

    public void update(Rol rol);

    public List<Rol> all();

}
