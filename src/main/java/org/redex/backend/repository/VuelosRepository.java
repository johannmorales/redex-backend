package org.redex.backend.repository;

import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VuelosRepository extends JpaRepository<Vuelo, Long> {

    @Query(""
            + "select v from Vuelo v "
            + "  join v.planVuelo pv "
            + "  join v.oficinaOrigen oo "
            + "  join v.oficinaDestino od "
            + "  join oo.pais oop "
            + "  join od.pais odp "
            + "where "
            + "  pv = :pvuelo and "
            + "  ( "
            + "    oop.nombre like %:q% or "
            + "    oop.codigo like %:q% or "
            + "    odp.nombre like %:q% or "
            + "    odp.codigo like %:q% or "
            + "    concat(oo.codigo, ' ',od.codigo) like %:q% or "
            + "    concat(oo.codigo, ' a ',od.codigo) like %:q% or "
            + "    concat(oop.codigo, ' ',odp.codigo) like %:q% or "
            + "    concat(oop.codigo, ' a ',odp.codigo) like %:q% or "
            + "    concat(oop.nombre, ' ',odp.nombre) like %:q% or "
            + "    concat(oop.nombre, ' a ',odp.nombre) like %:q% or "
            + "    oo.codigo like %:q% or "
            + "    od.codigo like %:q% "
            + "  )")
    Page<Vuelo> crimsonList(@Param("pvuelo") PlanVuelo pv, @Param("q") String q, Pageable pageable);
    
}
