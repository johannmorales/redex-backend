package org.redex.backend.repository;

import org.redex.model.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinasRepository extends JpaRepository<Oficina, Long>{
    
}
