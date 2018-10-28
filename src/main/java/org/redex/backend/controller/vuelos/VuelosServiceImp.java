package org.redex.backend.controller.vuelos;

import org.redex.backend.controller.planvuelo.VueloWrapper;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.repository.VuelosRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VuelosServiceImp implements VuelosService {

    @Autowired
    VuelosRepository vuelosRepository;

    @Override
    public Vuelo save(VueloWrapper wrapper) {
        Vuelo v = wrapper.build();
        v.setEstado(EstadoEnum.ACTIVO);
        vuelosRepository.save(v);
        
        return v;
    }

    @Override
    public Vuelo update(VueloWrapper wrapper) {
        Vuelo vueloBD = vuelosRepository.findById(wrapper.id)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo", "id", wrapper.id));

        Vuelo form = wrapper.build();

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
