package org.redex.backend.repository;

import org.redex.model.general.TipoDocumentoIdentidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDocIdentidadRepository extends JpaRepository<TipoDocumentoIdentidad, Long>{
    
}
