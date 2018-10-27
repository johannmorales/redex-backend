package org.redex.backend.repository;

import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.general.EstadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PlanVueloRepository extends JpaRepository<PlanVuelo, Long>{
    PlanVuelo findByEstado(EstadoEnum estado);
}
