/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.repository;

import org.redex.model.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author a20121302
 */
@Repository
@RequestMapping("rol")
public interface RolesRepository extends JpaRepository<Rol, Long> {
    
}
