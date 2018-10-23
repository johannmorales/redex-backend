package org.redex.backend.controller.planvuelo;

import java.io.IOException;
import static java.lang.Character.isDigit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.envios.PlanVuelo;
import org.redex.model.envios.Vuelo;
import org.redex.model.general.Archivo;
import org.redex.model.general.Pais;
import org.redex.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.redex.backend.repository.PlanVueloRepository;
import org.redex.backend.repository.VuelosRepository;
import org.redex.model.general.EstadoEnum;

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
    public CargaDatosResponse carga(Archivo archivo) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        //buscar el archivo en BD, si no esta lanza una expcepcion que hara que se le responda a cliente con un 404 not found
        Archivo archivoBD = archivosRepository.findById(archivo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado"));

        //obtener la ruta del archivo en el servidor
        Path path = Paths.get(archivoBD.getDirectorio())
                .toAbsolutePath().normalize();

        Path filePath = path.resolve(archivoBD.getNombreServidor()).normalize();
        
        //hashmap de paises por el codigo
        Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));
        
        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));
       
        //para guardar las oficinas que luego iran a bd
        List<Vuelo> nuevosVuelos = new ArrayList<>();
        
        PlanVuelo planVueloPasado = planVueloRepository.findByEstado(EstadoEnum.ACTIVO);
        if (planVueloPasado != null){
            planVueloPasado.setEstado(EstadoEnum.INACTIVO);
            planVueloRepository.save(planVueloPasado);
        }
        
        PlanVuelo pV = new PlanVuelo();
        pV.setEstado(EstadoEnum.ACTIVO);
        
        try (Stream<String> lineas = Files.lines(filePath)) {
            List<String> lineasList = lineas.collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                // si le vas a poner validacoines aqui deberias controlarlas
                // cambios para archivo con codigo de 3 caracteres
                if (!linea.isEmpty() && (linea.length()== 19)){
                    
                    String codeOffice1 = linea.substring(0, 3);
                    String codeOffice2 = linea.substring(4, 7);
                    String horaIni = linea.substring(8, 13);
                    String horaFin = linea.substring(14);
                    Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");
                    Matcher m1 = p.matcher(horaIni);
                    Matcher m2 = p.matcher(horaFin);
                    if ( codeOffice1.matches("[A-Z]+") && codeOffice2.matches("[A-Z]+")
                            && m1.matches() && m2.matches()){
                        nuevosVuelos.add(leerVuelo(codeOffice1, codeOffice2,
                                horaIni, horaFin, pV, oficinas));
                    }else{
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea "+ contLinea +" tiene formato incorrecto");
                    }
                } else {
                    cantidadErrores = cantidadErrores + 1;
                    errores.add("La linea "+ contLinea +" tiene formato incorrecto");
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
                
            Set<Vuelo> vuelos = new HashSet<Vuelo>(nuevosVuelos);
            pV.setVuelos(vuelos);
            
            for(Vuelo vuelo: nuevosVuelos){
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
    public void desactivarVuelo(Vuelo vuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void activarVuelo(Vuelo vuelo) {
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
        
        return vuelo;
    }
}
