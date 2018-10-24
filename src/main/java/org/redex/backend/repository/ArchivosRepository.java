package org.redex.backend.repository;

import org.redex.backend.model.general.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivosRepository extends JpaRepository<Archivo, Long>{
    
}
