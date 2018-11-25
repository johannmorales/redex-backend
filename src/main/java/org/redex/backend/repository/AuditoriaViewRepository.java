package org.redex.backend.repository;

import org.redex.backend.model.auditoria.AuditoriaView;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaViewRepository    extends JpaRepository<AuditoriaView, Long> {

    @Query("" +
            " select av from AuditoriaView av" +
            "   inner join fetch av.oficina o " +
            "   inner join fetch av.usuario u " +
            "   inner join fetch u.colaborador c " +
            "   inner join fetch c.persona per" +
            " where " +
            "   av.momento >= :inicio and " +
            "   av.momento <= :fin and " +
            "   o = :oficina " +
            " order by av.momento asc")
    List<AuditoriaView> allByOficinaVentana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("oficina") Oficina oficina
    );
}

