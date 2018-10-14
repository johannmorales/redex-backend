package org.redex.backend.repository;

import java.util.List;
import java.util.Optional;
import org.redex.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("usuarios")
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByIdIn(List<Long> userIds);

    Optional<Usuario> findByUsername(String username);

    Boolean existsByUsername(String username);

}
