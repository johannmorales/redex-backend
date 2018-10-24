package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.general.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisesRepository extends JpaRepository<Pais, Long> {

    List<Pais> findTop15ByNombreContaining(String nombre);
    
}