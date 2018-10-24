package org.redex.backend.repository;

import java.util.List;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinasRepository extends JpaRepository<Oficina, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Oficina o set o.capacidadActual = o.capacidadActual + 1 where o.id = :#{#oficina.id}")
    public void incrementarCapacidadActual(@Param("oficina") Oficina oficina);
    
    public List<Oficina> findAllByEstado(EstadoEnum estado);
    
}
