package org.redex.backend.controller.planvuelo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.rrhh.Oficina;

public class VueloWrapper {

    public Long id;

    public Long planVuelo;

    public Long oficinaOrigen;

    public Long oficinaDestino;

    public String horaInicio;

    public String horaFin;
    
    public Integer capacidad;

    public Vuelo build() {
        Vuelo v = new Vuelo();
        v.setId(id);
        v.setHoraFin(LocalTime.parse(horaFin, DateTimeFormatter.ofPattern("HH:mm")));
        v.setHoraInicio(LocalTime.parse(horaInicio, DateTimeFormatter.ofPattern("HH:mm")));
        v.setOficinaDestino(new Oficina(oficinaDestino));
        v.setOficinaOrigen(new Oficina(oficinaOrigen));
        v.setPlanVuelo(new PlanVuelo(planVuelo));
        v.setCapacidad(capacidad);
        return v;
    }
    
    public static VueloWrapper of(Vuelo v){
        VueloWrapper w = new VueloWrapper();
        w.id = v.getId();
        w.planVuelo = v.getPlanVuelo().getId();
        w.oficinaDestino = v.getOficinaDestino().getId();
        w.oficinaOrigen = v.getOficinaOrigen().getId();
        w.horaFin = v.getHoraFinString();
        w.horaInicio = v.getHoraInicioString();
        w.capacidad = v.getCapacidad();
        return w;
    }

}
