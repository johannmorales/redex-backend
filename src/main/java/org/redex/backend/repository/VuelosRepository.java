package org.redex.backend.repository;

import org.redex.model.envios.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VuelosRepository extends JpaRepository<Vuelo, Long> {
    
}
