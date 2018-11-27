package org.redex.backend.repository;

import java.util.List;
import java.util.Optional;
import org.redex.backend.model.seguridad.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByIdIn(List<Long> userIds);

    Optional<Usuario> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(""
            + "select u from Usuario u "
            + "  join u.rol r "
            + "  join u.colaborador c "
            + "  join c.persona p "
            + "  join p.tipoDocumentoIdentidad tdoc "
            + "  join p.tipoDocumentoIdentidad tdoc "
            + "where "
            + "  r.nombre like %:q% or "
            + "  concat(p.nombres, ' ', p.materno, ' ', p.paterno) like %:q% or "
            + "  concat(p.paterno, ' ', p.materno, ' ', p.nombres) like %:q% or "
            + "  concat(p.nombres, ' ', p.materno, ' ', p.paterno) like %:q% or "
            + "  concat(tdoc.simbolo, ' ', p.numeroDocumentoIdentidad) like %:q% "
            + "order by u.id desc ")
    Page<Usuario> crimsonList(@Param("q") String q, Pageable pageable);

}
