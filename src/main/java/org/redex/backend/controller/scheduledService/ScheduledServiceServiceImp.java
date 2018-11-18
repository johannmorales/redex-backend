package org.redex.backend.controller.scheduledService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.PaqueteEstadoEnum;
import org.redex.backend.model.envios.PaqueteRuta;
import org.redex.backend.model.envios.RutaEstadoEnum;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.envios.VueloAgendadoEstadoEnum;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaqueteRutaRepository;
import org.redex.backend.repository.PaquetesRepository;
import org.redex.backend.repository.PlanVueloRepository;
import org.redex.backend.repository.VuelosAgendadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduledServiceServiceImp implements ScheduledServiceService {

    @Autowired
    PlanVueloRepository planVueloRepository;

    @Autowired
    VuelosAgendadosRepository vuelosRepository;

    @Autowired
    PaquetesRepository paquetesRepository;

    @Autowired
    OficinasRepository oficinasRepository;

    @Autowired
    PaqueteRutaRepository paqueteRutasRepository;

    @Transactional
    public void salidaVuelos() {

        List<VueloAgendado> vuelos = vuelosRepository.findAll();

        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));

        LocalDateTime actualTime = LocalDateTime.now();

        for (VueloAgendado vA : vuelos) {
            if ((vA.getEstado().equals(VueloAgendadoEstadoEnum.CREADO))
                    && (vA.getFechaInicio().isBefore(actualTime))) {
                vA.setEstado(VueloAgendadoEstadoEnum.ACTIVO);
                Oficina o = oficinas.get(vA.getOficinaOrigen().getCodigo());
                List<PaqueteRuta> pR = paqueteRutasRepository.findAllByVueloAgendado(vA);
                for (PaqueteRuta p : pR) {
                    p.setEstado(RutaEstadoEnum.ACTIVO);
                    Paquete px = p.getPaquete();
                    px.setEstado(PaqueteEstadoEnum.EN_VUELO);
                    o.setCapacidadActual(o.getCapacidadActual() - 1);
                    paqueteRutasRepository.save(p);
                    paquetesRepository.save(px);
                }
                vuelosRepository.save(vA);
                oficinasRepository.save(o);
            }
        }

    }

    @Transactional
    public void llegadaVuelos() {

        List<VueloAgendado> vuelos = vuelosRepository.findAll();
        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));
        LocalDateTime actualTime = LocalDateTime.now();

        for (VueloAgendado vA : vuelos) {
            if (!(vA.getEstado().equals(VueloAgendadoEstadoEnum.FINALIZADO))
                    && (vA.getFechaInicio().isAfter(actualTime))) {
                vA.setEstado(VueloAgendadoEstadoEnum.FINALIZADO);
                Oficina o = oficinas.get(vA.getOficinaDestino());
                List<PaqueteRuta> pR = paqueteRutasRepository.findAllByVueloAgendado(vA);
                for (PaqueteRuta p : pR) {
                    p.setEstado(RutaEstadoEnum.FINALIZADO);
                    paqueteRutasRepository.save(p);
                    Paquete px = p.getPaquete();
                    List<PaqueteRuta> pR1 = paqueteRutasRepository.findAllByPaquete(px);
                    int termino = 0;
                    for (PaqueteRuta pAux : pR1) {
                        if (!pAux.getEstado().equals(RutaEstadoEnum.FINALIZADO)) {
                            termino = 1;
                        }
                    }
                    if (termino == 1) {
                        px.setEstado(PaqueteEstadoEnum.ENTREGADO);
                    } else {
                        o.setCapacidadActual(o.getCapacidadActual() + 1);
                        px.setEstado(PaqueteEstadoEnum.EN_ALMACEN);
                    }
                    paquetesRepository.save(px);
                }
                vuelosRepository.save(vA);
                oficinasRepository.save(o);
            }
        }
    }
}