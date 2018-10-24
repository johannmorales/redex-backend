package org.redex.backend.repository;

import org.redex.backend.model.envios.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaquetesRepository extends JpaRepository<Paquete, Long> {

}
