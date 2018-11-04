package org.redex.backend.repository;

import org.redex.backend.model.envios.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaquetesRepository extends JpaRepository<Paquete, Long> {

    @Override
    @Query("" +
            " select paq from Paquete paq " +
            "   join fetch paq.personaOrigen po " +
            "   join fetch paq.personaDestino pd " +
            "   join fetch paq.oficinaOrigen oo " +
            "   join fetch paq.oficinaDestino od " +
            "   join fetch oo.pais " +
            "   join fetch od.pais " +
            " order by paq.id desc ")
    List<Paquete> findAll();
}
