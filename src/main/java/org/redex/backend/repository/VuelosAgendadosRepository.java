package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.envios.VueloAgendado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VuelosAgendadosRepository extends JpaRepository<VueloAgendado, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE VueloAgendado va set va.capacidadActual = va.capacidadActual + 1 where va.id = :#{#vueloAgendado.id}")
    public void incrementarCapacidadActual(@Param("vueloAgendado") VueloAgendado vueloAgendado);

    @Query(""
            + "select va from VueloAgendado va "
            + "  join va.vuelo v "
            + "  join v.oficinaOrigen oo "
            + "  join v.oficinaDestino od "
            + "  join oo.pais oop "
            + "  join od.pais odp "
            + "where "
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
            + "  ) "
            + "  order by va.id desc")
    public Page<VueloAgendado> crimsonList(@Param("q") String q, Pageable pageable);

    public List<VueloAgendado> findAllByFechaInicioAfter(LocalDateTime fechaInicio);

    @Query(" "
            + "select va from VueloAgendado va "
            + " join fetch va.vuelo v"
            + " join fetch v.oficinaOrigen oo "
            + " join fetch v.oficinaDestino od "
            + "where "
            + " va.fechaInicio > :inicio and"
            + " va.fechaFin < :fin ")
    public List<VueloAgendado> findAllByFechaInicioAfterAndFechaFinBefore(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Override
    @Query(" select va from VueloAgendado va "
            + " join fetch va.vuelo v"
            + " join fetch v.oficinaOrigen oo "
            + " join fetch v.oficinaDestino od ")
    public List<VueloAgendado> findAll();

}
