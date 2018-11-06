/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.repository;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author Oscar
 */

@Repository
public interface SimulacionVuelosRepository extends JpaRepository<SimulacionVuelo, Long>{

    @Query("" +
            " select v from SimulacionVuelo v" +
            " where" +
            "   v.simulacion = :s " +
            " order by  v.horaInicio asc ")
    List<SimulacionVuelo> findAllBySimulacion(@Param("s") Simulacion simulacion);

}
