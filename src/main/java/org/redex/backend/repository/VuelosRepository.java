package org.redex.backend.repository;

import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VuelosRepository extends JpaRepository<Vuelo, Long> {

    Page<Vuelo> findAllByPlanVuelo(PlanVuelo pv, Pageable pageable);
    
}
