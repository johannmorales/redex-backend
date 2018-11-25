package org.redex.backend.controller.vuelosagendados;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.Local;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.repository.PlanVueloRepository;
import org.redex.backend.repository.VuelosAgendadosRepository;
import org.redex.backend.repository.VuelosRepository;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VuelosAgendadosServiceImp implements VuelosAgendadosService {

    private static Logger logger = LogManager.getLogger(VuelosAgendadosServiceImp.class);
    
    @Autowired
    PlanVueloRepository planVueloRepository;
    
    @Autowired
    VuelosRepository vuelosRepository;
    
    @Autowired
    VuelosAgendadosRepository vuelosAgendadosRepository;
    
    @Override
    public Page<VueloAgendado> crimsonList(CrimsonTableRequest request, DataSession ds) {
        switch (ds.getRol().getCodigo()){
            case EMPLEADO:
            case JEFE_OFICINA:
                return vuelosAgendadosRepository.crimsonListLimitado(request.getSearch(), request.createPagination(), ds.getOficina());
            case ADMINISTRADOR:
            case GERENTE_GENERAL:
                return vuelosAgendadosRepository.crimsonList(request.getSearch(), request.createPagination());
        }
        return vuelosAgendadosRepository.crimsonList(request.getSearch(), request.createPagination());
    }

    @Override
    @Transactional
    public Integer generar(GenerarPayload payload) {
        PlanVuelo pv = planVueloRepository.findByEstado(EstadoEnum.ACTIVO);
        
        if(pv == null ) {
            throw new AppException("No hay un plan de vuelo activo");
        }
        
        List<Vuelo> vuelos = vuelosRepository.findAllByPlanVueloAndEstado(pv, EstadoEnum.ACTIVO);
        
        Integer cont = 0;
        
        while(!payload.inicio.isAfter(payload.fin)){
            for (Vuelo vuelo : vuelos) {
                  VueloAgendado va = VueloAgendado.of(vuelo, payload.inicio);
                  
                  vuelosAgendadosRepository.save(va);
                  cont++;
            }
            payload.inicio = payload.inicio.plus(1, ChronoUnit.DAYS);
        }
        return cont;
    }

    @Override
    @Transactional
    public void eliminarInnecesarios() {
        vuelosAgendadosRepository.deleteAllBeforeFecha(LocalDateTime.now());
    }

}
