package org.redex.backend.controller.roles;

import java.util.List;
import org.redex.backend.model.seguridad.Rol;
import org.redex.backend.repository.RolesRepository;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RolesServiceImp implements RolesService {

    @Autowired
    RolesRepository rolesRepository;
    
    @Override
    public Page<Rol> crimsonList(CrimsonTableRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void find(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void save(Rol rol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void update(Rol rol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Rol> all() {
        return rolesRepository.findAll();
    }

}
