package org.redex.backend.repository;

import org.redex.model.Continente;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("continentes")
public interface ContinenteRepository extends PagingAndSortingRepository<Continente, Long>{
    
}
