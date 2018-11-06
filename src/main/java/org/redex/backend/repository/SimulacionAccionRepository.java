package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionAccionRepository extends JpaRepository<SimulacionAccion, Long> {

    @Query("" +
            " select a from SimulacionAccion a " +
            "   join a.simulacion s " +
            " where " +
            "   s = :s " +
            " order by a.fechaInicio asc ")
    List<SimulacionAccion> findAllBySimulacion(@Param("s") Simulacion simulacion);

    @Query("" +
            " select a from SimulacionAccion a " +
            "   join a.simulacion s " +
            " where " +
            "   s = :s and " +
            "   ( a.tipo = 'REGISTRO' and a.fechaInicio >= :inicio and a.fechaInicio < :fin ) or " +
            "   ( a.tipo = 'SALIDA'   and a.fechaInicio >= :inicio and a.fechaFin < :fin ) " +
            " order by a.fechaInicio asc ")
    List<SimulacionAccion> findAllBySimulacionVentana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );
}
