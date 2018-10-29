package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionAccionRepository extends JpaRepository<SimulacionAccion, Long> {
    
    List<SimulacionAccion> findAllBySimulacion(Simulacion simulacion);
    
}
