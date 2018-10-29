package org.redex.backend.repository;

import org.redex.backend.model.simulacion.SimulacionOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionOficinasRepository extends JpaRepository<SimulacionOficina, Long> {
    
}
