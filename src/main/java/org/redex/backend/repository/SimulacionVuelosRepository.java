/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.repository;

import org.redex.backend.model.simulacion.SimulacionVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Oscar
 */

@Repository
public interface SimulacionVuelosRepository extends JpaRepository<SimulacionVuelo, Long>{
    
}