package org.redex.backend.repository;

import org.redex.model.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Rol, Long> {
    
}
