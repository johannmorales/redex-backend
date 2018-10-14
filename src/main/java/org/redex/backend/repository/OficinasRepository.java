package org.redex.backend.repository;

import org.redex.model.rrhh.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("oficinas")
public interface OficinasRepository extends JpaRepository<Oficina, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Oficina o set o.capacidadActual = o.capacidadActual + 1 where o.id = :#{#oficina.id}")
    public void incrementarCapacidadActual(@Param("oficina") Oficina oficina);

}
