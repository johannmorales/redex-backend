package org.redex.backend.controller.simulacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.PaqueteEstadoEnum;
import org.redex.backend.model.general.Pais;
import org.redex.backend.model.general.Persona;
import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SimulacionServiceImp implements SimulacionService {

    @Autowired
    OficinasRepository oficinasRepository;
    
    @Override
    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file) {
        //Simulacion simu = simuRepo.findById(id).orElseThrow .... 
        
        //simuPaquete.setSimulacion(simu)
        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));
        
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();
        List<Paquete> nuevosPaquetes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            
            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty()) {
                    List<String> separateLine = Arrays.asList(linea.split("-"));
                    if (separateLine.size() == 22) {
                        Paquete nuevoP = leePaquete(separateLine, oficinas);
                        nuevosPaquetes.add(nuevoP);
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene todos los campos");
                    }
                }
                
                nuevosPaquetes.forEach((paquete) -> {
                    //paquetesRepository.save(paquete);
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
        
    }

    private Paquete leePaquete( List<String> datos,Map<String, Oficina> oficinas){
        Paquete p = new Paquete();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(datos.get(1).substring(0, 4)+"-"+
                datos.get(1).substring(4, 6)+"-"+datos.get(1).substring(6, 8)+
                " "+datos.get(2).substring(0, 2)+datos.get(2).substring(2), formatter);
        date = date.plus(-5, ChronoUnit.HOURS);
        System.out.println(datos.get(0).substring(0,4));
        
        p.setCodigoRastreo(datos.get(0));
        p.setEstado(PaqueteEstadoEnum.REGISTRADO);
        p.setFechaIngreso(ZonedDateTime.of(date, ZoneId.of("UTC")));
        p.setOficinaDestino(oficinas.get(datos.get(3)));
        p.setOficinaOrigen(oficinas.get(datos.get(0).substring(0,4)));
        return p;
    }
    
}
