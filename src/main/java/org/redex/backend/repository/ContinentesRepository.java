package org.redex.backend.repository;

import org.redex.model.Continente;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentesRepository extends PagingAndSortingRepository<Continente, Long>{
    
}
