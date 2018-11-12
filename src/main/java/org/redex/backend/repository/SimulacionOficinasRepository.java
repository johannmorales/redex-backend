package org.redex.backend.repository;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulacionOficinasRepository extends JpaRepository<SimulacionOficina, Long> {

    @Override
    List<SimulacionOficina> findAll();

    @Query("" +
            " select so from SimulacionOficina so" +
            "   join fetch so.pais p " +
            " where so.simulacion = :s")
    List<SimulacionOficina> findAllBySimulacion(@Param("s") Simulacion simulacion);

    void deleteBySimulacion(Simulacion simulacion);

}
