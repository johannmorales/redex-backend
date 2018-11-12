package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionAccionRepository extends JpaRepository<SimulacionAccion, Long> {

    @Query("" +
            " select a from SimulacionAccion a " +
            "   join a.simulacion s " +
            " where " +
            "   s = :s " +
            " order by a.fechaInicio asc ")
    List<SimulacionAccion> findAllBySimulacion(@Param("s") Simulacion simulacion);

    @Query("" +
            " select a from SimulacionAccion a " +
            "   join a.simulacion s" +
            "   left join fetch  a.oficinaOrigen oo " +
            "   left join fetch  oo.pais oop " +
            "   left join fetch  a.oficinaDestino od " +
            "   left join fetch  od.pais odp " +
            " where " +
            "   s = :s and " +
            "   ( a.tipo = 'REGISTRO' and a.fechaInicio >= :inicio and a.fechaInicio < :fin ) or " +
            "   ( a.tipo = 'SALIDA'   and a.fechaInicio >= :inicio and a.fechaInicio < :fin ) " +
            " order by a.fechaInicio asc ")
    List<SimulacionAccion> findAllBySimulacionVentana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );

//    @Modifying(clearAutomatically = true)
//    @Query("" +
//            " insert into  SimulacionAccion (simulacion, oficinaOrigen, oficinaDestino, tipo, fechaInicio, fechaFin, cantidad, cantidadSalida ) " +
//            " select sva.simulacion, v.oficinaOrigen, v.oficinaDestino, SimulacionAccionTipoEnum.SALIDA, sva.fechaInicio, sva.fechaFin, sva.capacidadActual, sva.cantidadSalida " +
//            " from SimulacionVueloAgendado sva" +
//            "   join sva.vuelo v " +
//            " where " +
//            "   sva.simulacion = :s and " +
//            "   sva.fechaInicio >= :inicio and " +
//            "   sva.fechaFin < :fin ")
//    void generarAccionesVueloAgendado(
//            @Param("inicio") LocalDateTime inicio,
//            @Param("fin") LocalDateTime fin,
//            @Param("s") Simulacion simulacion
//    );

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "" +
            " insert into  simulacion_accion (id_simulacion, id_oficina_origen, id_oficina_destino, tipo, fecha_inicio, fecha_fin, cantidad, cantidad_salida ) " +
            " select sva.id_simulacion, v.id_oficina_origen, v.id_oficina_destino, 'SALIDA', sva.fecha_inicio, sva.fecha_fin, sva.capacidad_actual, sva.cantidad_salida " +
            " from simulacion_vuelo_agendado sva" +
            "   join simulacion_vuelo  v on sva.id_vuelo = v.id " +
            " where " +
            "   sva.id_simulacion = :s and " +
            "   sva.fecha_inicio >= :inicio and " +
            "   sva.fecha_inicio < :fin and " +
            "   sva.accion_generada = 0  ")
    void generarAccionesVueloAgendado(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("s") Simulacion simulacion
    );


    void deleteBySimulacion(Simulacion simulacion);

}
