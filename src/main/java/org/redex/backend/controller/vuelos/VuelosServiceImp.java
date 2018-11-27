package org.redex.backend.controller.vuelos;

import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.repository.PlanVueloRepository;
import org.redex.backend.repository.VuelosRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VuelosServiceImp implements VuelosService {

    @Autowired
    PlanVueloRepository planVueloRepository;

    @Autowired
    VuelosRepository vuelosRepository;

    @Override
    @Transactional
    public Vuelo save(Vuelo wrapper) {
        Vuelo v = wrapper;
        v.setEstado(EstadoEnum.ACTIVO);
        v.setPlanVuelo(planVueloRepository.findByEstado(EstadoEnum.ACTIVO));
        vuelosRepository.save(v);

        return v;
    }

    @Override
    @Transactional
    public Vuelo update(Vuelo wrapper) {
        Vuelo vueloBD = vuelosRepository.findById(wrapper.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo", "id", wrapper.getId()));

        Vuelo form = wrapper;

        vueloBD.setOficinaDestino(form.getOficinaDestino());
        vueloBD.setOficinaOrigen(form.getOficinaOrigen());
        vueloBD.setHoraFin(form.getHoraFin());
        vueloBD.setHoraInicio(form.getHoraInicio());
        vueloBD.setCapacidad(form.getCapacidad());

        vuelosRepository.save(vueloBD);

        return vueloBD;
    }

    @Override
    public Vuelo find(Long id) {
        return vuelosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo", "id", id));
    }
}
