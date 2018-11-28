package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    void incrementarCapacidadActual(@Param("vueloAgendado") VueloAgendado vueloAgendado);

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
    Page<VueloAgendado> crimsonList(@Param("q") String q, Pageable pageable);


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
            + "  ) and " +
            "    ( oo = :oficina or od = :oficina )"
            + "  order by va.id desc")
    Page<VueloAgendado> crimsonListLimitado(@Param("q") String search, Pageable pageable, @Param("oficina") Oficina oficina);


    @Query("" +
            " select sva from VueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od " +
            " where " +
            "   sva.fechaInicio <= :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.capacidadActual < sva.capacidadMaxima " +
            " order by sva.fechaFin asc "
    )
    List<VueloAgendado> findAllTerminados(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);


    @Query(" " +
            " select sva from VueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od " +
            " where " +
            "   sva.fechaInicio > :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.capacidadActual < sva.capacidadMaxima " +
            " order by sva.fechaInicio asc "
    )
    List<VueloAgendado> findAllAlgoritmo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query(" " +
            " select sva from VueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od " +
            " where " +
            "   sva.fechaInicio >= :inicio and " +
            "   sva.fechaInicio <= :fin and " +
            "   sva.capacidadActual < sva.capacidadMaxima " +
            " order by sva.fechaInicio asc "
    )
    List<VueloAgendado> findAllParten(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query(" " +
            " select sva from VueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od " +
            " where " +
            "   sva.fechaFin >= :inicio and " +
            "   sva.fechaFin <= :fin and " +
            "   sva.capacidadActual < sva.capacidadMaxima " +
            " order by sva.fechaInicio asc "
    )
    List<VueloAgendado> findAllLlegan(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Override
    @Query(" select va from VueloAgendado va "
            + " join fetch va.vuelo v"
            + " join fetch v.oficinaOrigen oo "
            + " join fetch v.oficinaDestino od ")
    List<VueloAgendado> findAll();

    @Modifying(clearAutomatically = true)
    @Query(" delete from VueloAgendado va where capacidadActual = 0 and fecha_fin < :fecha ")
    void deleteAllBeforeFecha(@Param("fecha") LocalDateTime fecha);


}
