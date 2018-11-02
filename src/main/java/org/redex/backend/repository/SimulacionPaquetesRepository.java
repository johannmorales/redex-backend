package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionPaquetesRepository extends JpaRepository<SimulacionPaquete, Long> {

    public List<SimulacionPaquete> findAllBySimulacionAndFechaSalidaBetween(Simulacion simulacion, LocalDateTime inicio, LocalDateTime fin);

    public List<SimulacionPaquete> findAllBySimulacionAndFechaIngresoBetween(Simulacion simulacion, LocalDateTime inicio, LocalDateTime fin);
    
}
