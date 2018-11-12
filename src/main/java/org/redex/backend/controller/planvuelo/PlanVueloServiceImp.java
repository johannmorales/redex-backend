package org.redex.backend.controller.planvuelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.envios.PlanVuelo;
import org.redex.backend.model.envios.Vuelo;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.redex.backend.repository.PlanVueloRepository;
import org.redex.backend.repository.VuelosRepository;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class PlanVueloServiceImp implements PlanVueloService {

    @Autowired
    OficinasRepository oficinasRepository;

    @Autowired
    ArchivosRepository archivosRepository;

    @Autowired
    PaisesRepository paisesRepository;

    @Autowired
    PlanVueloRepository planVueloRepository;

    @Autowired
    VuelosRepository vuelosRepository;

    @Override
    @Transactional
    public CargaDatosResponse carga(MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));

        List<Vuelo> nuevosVuelos = new ArrayList<>();

        PlanVuelo planVueloPasado = planVueloRepository.findByEstado(EstadoEnum.ACTIVO);
        if (planVueloPasado != null) {
            planVueloPasado.setEstado(EstadoEnum.INACTIVO);
            planVueloRepository.save(planVueloPasado);
        }

        PlanVuelo pV = new PlanVuelo();
        pV.setEstado(EstadoEnum.ACTIVO);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                // si le vas a poner validacoines aqui deberias controlarlas
                // cambios para archivo con codigo de 3 caracteres
                if (!linea.isEmpty() && (linea.length() == 21)) {

                    String codeOffice1 = linea.substring(0, 4);
                    String codeOffice2 = linea.substring(5, 9);
                    String horaIni = linea.substring(10, 15);
                    String horaFin = linea.substring(16);
                    Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");
                    Matcher m1 = p.matcher(horaIni);
                    Matcher m2 = p.matcher(horaFin);
                    if (codeOffice1.matches("[A-Z]+") && codeOffice2.matches("[A-Z]+")
                            && m1.matches() && m2.matches()) {
                        nuevosVuelos.add(leerVuelo(codeOffice1, codeOffice2,
                                horaIni, horaFin, pV, oficinas));
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " tiene formato incorrecto");
                    }
                } else {
                    cantidadErrores = cantidadErrores + 1;
                    errores.add("La linea " + contLinea + " tiene formato incorrecto");
                }
                contLinea++;
            }
//            try{
//                planVueloRepository.save(planVueloPasado);
//                planVueloRepository.save(pV);
//            } catch (Exception ex) {
//                cantidadErrores++;
//                errores.add("Erorr de integridad de datos");
//            }

            planVueloRepository.save(pV);

            pV.setVuelos(nuevosVuelos);

            for (Vuelo vuelo : nuevosVuelos) {
//                try {
//                    vuelosRepository.save(vuelo);
//                    cantidadRegistros++;
//                } catch (Exception ex) {
//                    cantidadErrores++;
//                    errores.add("Erorr de integridad de datos");
//                }
                vuelosRepository.save(vuelo);
                cantidadRegistros++;
            }

//            try{
//                planVueloRepository.save(planVueloPasado);
//                planVueloRepository.save(pV);
//            } catch (Exception ex) {
//                cantidadErrores++;
//                errores.add("Erorr de integridad de datos");
//            }
            planVueloRepository.save(pV);

        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    @Override
    public void guardar(PlanVuelo planVuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editarVuelo(Vuelo vuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizarVuelo(Vuelo vuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Vuelo leerVuelo(String codeOffice1, String codeOffice2,
            String horaIni, String horaFin, PlanVuelo pV, Map<String, Oficina> mapOficinas) {
        // codigo para leer una oficina de una linea del archivo 

        Vuelo vuelo = new Vuelo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        vuelo.setPlanVuelo(pV);
        vuelo.setOficinaOrigen(mapOficinas.get(codeOffice1));
        vuelo.setOficinaDestino(mapOficinas.get(codeOffice2));
        vuelo.setHoraInicio(LocalTime.parse(horaIni, dateTimeFormatter));
        System.out.println(LocalTime.parse(horaFin, dateTimeFormatter));
        vuelo.setHoraFin(LocalTime.parse(horaFin, dateTimeFormatter));
        vuelo.setEstado(EstadoEnum.ACTIVO);
        vuelo.setCapacidad(500);
        
        if(vuelo.getOficinaOrigen() == null) {
            throw new AppException(String.format("Oficina con codigo %s no encontrada", codeOffice1));
        }
        
        if(vuelo.getOficinaDestino() == null) {
            throw new AppException(String.format("Oficina con codigo %s no encontrada", codeOffice2));
        }

        return vuelo;
    }

    @Override
    public PlanVuelo findActivo() {
        return planVueloRepository.findByEstado(EstadoEnum.ACTIVO);
    }

    @Override
    @Transactional
    public void desactivarVuelo(Long id) {
        Vuelo vuelo = vuelosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        vuelo.setEstado(EstadoEnum.INACTIVO);
        vuelosRepository.save(vuelo);
    }

    @Override
    @Transactional
    public void activarVuelo(Long id) {
        Vuelo vuelo = vuelosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        vuelo.setEstado(EstadoEnum.ACTIVO);
        vuelosRepository.save(vuelo);
    }

    @Override
    public Page<Vuelo> allByCrimson(CrimsonTableRequest request) {
        PlanVuelo pv = planVueloRepository.findByEstado(EstadoEnum.ACTIVO);
        
        if(pv == null){
            return Page.empty();
        }
        
        return vuelosRepository.crimsonList(pv, request.getSearch(), request.createPagination());
    }

}
