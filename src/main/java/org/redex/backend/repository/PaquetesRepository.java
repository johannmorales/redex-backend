package org.redex.backend.repository;

import org.redex.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("paquetes")
public interface PaquetesRepository extends JpaRepository<Paquete, Long>{
    
}
