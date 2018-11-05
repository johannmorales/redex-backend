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
    
    List<SimulacionAccion> findAllBySimulacion(Simulacion simulacion);

    @Query("" +
            " select a from SimulacionAccion a " +
            "   join a.simulacion s " +
            " where " +
            "   s.id = :s and " +
            "   fechaInicio >= :inicio and " +
            "   fechaFin < :fin " +
            " order by a.")
    List<SimulacionAccion> findAllBySimulacionVentana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );
}
