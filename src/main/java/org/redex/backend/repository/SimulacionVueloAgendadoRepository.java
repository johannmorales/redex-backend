package org.redex.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionVueloAgendadoRepository extends JpaRepository<SimulacionVueloAgendado, Long>{

    public List<SimulacionVueloAgendado> findAllBySimulacionAndFechaInicioBetween(Simulacion simulacion, LocalDateTime inicio, LocalDateTime fin);
    
}
