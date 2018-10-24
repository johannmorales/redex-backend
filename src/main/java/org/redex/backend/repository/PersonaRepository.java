/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.repository;

import org.redex.model.general.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Oscar
 */
@Repository
@RequestMapping("persona")
public interface PersonaRepository extends JpaRepository<Persona, Long>{
    
}