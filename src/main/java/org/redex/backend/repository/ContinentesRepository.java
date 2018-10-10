package org.redex.backend.repository;

import org.redex.model.Continente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentesRepository extends JpaRepository<Continente, Long>{
    
}
