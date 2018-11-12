package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionVueloAgendadoRepository extends JpaRepository<SimulacionVueloAgendado, Long> {

    @Query(" " +
            " select sva from SimulacionVueloAgendado sva " +
            "   join fetch sva.vuelo v " +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od" +
            "   join fetch oo.pais po " +
            "   join fetch od.pais pd " +
            " where " +
            "   sva.fechaInicio > :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.capacidadActual < sva.capacidadMaxima and " +
            "   sva.simulacion = :s" +
            " order by sva.fechaInicio asc "
    )
    List<SimulacionVueloAgendado> findAllAlgoritmo(
            @Param("s") Simulacion simulacion,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    @Query(" " +
            " select sva from SimulacionVueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od" +
            "   join fetch oo.pais po " +
            "   join fetch od.pais pd " +
            " where " +
            "   sva.fechaInicio <= :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.simulacion = :s" +
            " order by sva.fechaFin asc "
    )
    List<SimulacionVueloAgendado> findAllAlgoritmoTerminan(
            @Param("s") Simulacion simulacion,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    @Modifying(clearAutomatically = true)
    @Query(" " +
            " UPDATE SimulacionVueloAgendado va " +
            " set " +
            "   va.capacidadActual = va.capacidadActual + 1,   " +
            "   va.cantidadSalida = va.cantidadSalida + 1 " +
            " where " +
            "   va = :va ")
    void agregarFinalPaquete(@Param("va") SimulacionVueloAgendado step);

    @Modifying(clearAutomatically = true)
    @Query(" " +
            " update SimulacionVueloAgendado va " +
            " set " +
            "   va.capacidadActual = va.capacidadActual + 1 " +
            " where " +
            "   va = :va ")
    void agregarPaquete(@Param("va") SimulacionVueloAgendado step);


    @Query(" " +
            " select sva from SimulacionVueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od" +
            "   join fetch oo.pais po " +
            "   join fetch od.pais pd " +
            "   join fetch po.continente " +
            "   join fetch pd.continente " +
            " where " +
            "   sva.fechaInicio >= :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.simulacion = :s " +
            " order by sva.fechaInicio asc "
    )
    List<SimulacionVueloAgendado> findAllByWindow(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );

    @Query(" " +
            " select sva from SimulacionVueloAgendado sva " +
            "   join fetch sva.vuelo v" +
            "   join fetch v.oficinaOrigen oo " +
            "   join fetch v.oficinaDestino od" +
            " where " +
            "   sva.fechaInicio >= :inicio and " +
            "   sva.fechaFin < :fin and " +
            "   sva.simulacion = :s and " +
            "   ( sva.accionGenerada = null or sva.accionGenerada = false ) " +
            " order by sva.fechaInicio asc "
    )
    List<SimulacionVueloAgendado> findAllByWindowGenerarAccion(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );

    @Modifying(clearAutomatically = true)
    @Query(" " +
            " update SimulacionVueloAgendado sva " +
            " set sva.accionGenerada = true" +
            " where " +
            "   sva.fechaInicio >= :inicio and " +
            "   sva.fechaInicio < :fin and " +
            "   sva.simulacion = :s")
    void marcarAccionGenerada(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );
    
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = ""
            + " insert into simulacion_vuelo_agendado (id_vuelo, fecha_inicio, fecha_fin, capacidad_actual, capacidad_maxima, accion_generada, id_simulacion) "
            + " select  "
            + "     id, "
            + "     concat( :fecha, SUBSTRING(hora_inicio,11) ), "
            + "     concat( IF( hora_inicio < hora_fin , :fecha, DATE_ADD( :fecha ,INTERVAL 1 DAY)), SUBSTRING(hora_fin,11)), "
            + "     0, "
            + "     capacidad ,"
            + "     0, "
            + "     id_simulacion "
            + " from simulacion_vuelo "
            + " where id_simulacion = :s ")
    void generarVuelos(@Param("fecha") String fecha, @Param("s") Long idSimulacion);


    @Modifying(clearAutomatically = true)
    @Query(value = ""
            + " update SimulacionVueloAgendado set accionGenerada = false, capacidadActual = 0, cantidadSalida = 0 where simulacion = :s ")
    void deleteBySimulacion(@Param("s") Simulacion simulacion);
}
