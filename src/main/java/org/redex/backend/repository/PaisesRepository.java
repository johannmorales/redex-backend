package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.general.Pais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisesRepository extends JpaRepository<Pais, Long> {

    List<Pais> findTop15ByNombreContaining(String nombre);

    @Query("select p from Pais p where p.nombre like %:q% or p.codigo like %:q% order by p.nombre asc")
    List<Pais> allByNombre(@Param("q") String q, Pageable pageable);

    
     @Query(""
            + "select p from Pais p "
            + "  join p.continente c "
            + "where "
            + "  p.codigo like %:q% or "
            + "  p.nombre like %:q% or "
            + "  p.codigoIso like %:q% or "
            + "  c.nombre like %:q% ")
    public Page<Pais> crimsonList(@Param("q") String q, Pageable pageable);
    
}