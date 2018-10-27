package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinasRepository extends JpaRepository<Oficina, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Oficina o set o.capacidadActual = o.capacidadActual + 1 where o.id = :#{#oficina.id}")
    void incrementarCapacidadActual(@Param("oficina") Oficina oficina);

    public List<Oficina> findAllByEstado(EstadoEnum estado);

    @Query("select o from Oficina o join o.pais p where o.codigo like %:q% or p.codigo like %:q% or p.nombre like %:q%")
    List<Oficina> customSearch(@Param(value = "q") String q);

    @Query("select o from Oficina o join o.pais p where o.codigo like %:q% or p.codigo like %:q% or p.nombre like %:q%")
    Page<Oficina> customPaginatedSearch(@Param(value = "q") String q, Pageable pageable);

}
