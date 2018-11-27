package org.redex.backend.dao.dashboard;

import org.redex.backend.model.dashboard.PaquetesVuelo;

import java.time.LocalDate;
import java.util.List;

public interface PaquetesVueloDAO {

    List<PaquetesVuelo> top(LocalDate inicio, LocalDate fin, Integer cantidad);

    List<PaquetesVuelo> bottom(LocalDate inicio, LocalDate fin, Integer cantidad);

}
