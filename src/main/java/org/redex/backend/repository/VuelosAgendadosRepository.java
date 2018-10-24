package org.redex.backend.repository;

import org.redex.backend.model.envios.VueloAgendado;
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

}
