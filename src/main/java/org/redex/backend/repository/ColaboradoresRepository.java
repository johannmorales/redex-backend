package org.redex.backend.repository;

import org.redex.model.rrhh.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("colaboradores")
public interface ColaboradoresRepository extends JpaRepository<Colaborador, Long> {

}
