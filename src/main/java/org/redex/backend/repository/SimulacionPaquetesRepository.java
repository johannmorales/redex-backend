package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionPaquetesRepository extends JpaRepository<SimulacionPaquete, Long> {

    public List<SimulacionPaquete> findAllBySimulacionAndFechaSalidaBetween(Simulacion simulacion, LocalDateTime inicio, LocalDateTime fin);

    @Query("" +
            " select p from SimulacionPaquete p " +
            "   join fetch p.oficinaOrigen oo " +
            "   join fetch p.oficinaDestino od " +
            "   join fetch oo.pais " +
            "   join fetch od.pais " +
            " where " +
            "   p.simulacion = :s and " +
            "   p.fechaIngreso >= :inicio and " +
            "   p.fechaIngreso < :fin ")
    List<SimulacionPaquete> findAllBySimulacionAndFechaIngresoBetween(@Param("s") Simulacion simulacion, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    void deleteBySimulacion(Simulacion simulacion);


}
