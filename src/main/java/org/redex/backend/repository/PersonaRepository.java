package org.redex.backend.repository;

import org.redex.backend.model.general.Persona;
import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query(""
            + "select p from Persona p "
            + "  join p.tipoDocumentoIdentidad tdoc "
            + "where "
            + "  concat(p.nombres, ' ', p.materno, ' ', p.paterno) like %:q% or "
            + "  concat(p.paterno, ' ', p.materno, ' ', p.nombres) like %:q% or "
            + "  concat(p.nombres, ' ', p.materno, ' ', p.paterno) like %:q% or "
            + "  concat(tdoc.simbolo, ' ', p.numeroDocumentoIdentidad) like %:q% ")
    Page<Persona> crimsonList(@Param("q") String q, Pageable pageable);

    public Persona findByTipoDocumentoIdentidadAndNumeroDocumentoIdentidad(TipoDocumentoIdentidad tipoDocumentoIdentidad, String numeroDocumentoIdentidad);

}
