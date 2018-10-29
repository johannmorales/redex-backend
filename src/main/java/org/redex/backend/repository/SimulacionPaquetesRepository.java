package org.redex.backend.repository;

import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionPaquetesRepository extends JpaRepository<SimulacionPaquete, Long> {
    
}
