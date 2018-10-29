package org.redex.backend.repository;

import org.redex.backend.model.simulacion.Simulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionRepository extends JpaRepository<Simulacion, Long> {
    
}
