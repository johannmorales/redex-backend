/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.PaqueteRuta;
import org.redex.backend.model.envios.VueloAgendado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Oscar
 */
@Repository
public interface PaqueteRutaRepository extends JpaRepository<PaqueteRuta, Long>{
    
    public List<PaqueteRuta> findAllByVueloAgendado(VueloAgendado va);
    
    public List<PaqueteRuta> findAllByPaquete(Paquete p);
}
