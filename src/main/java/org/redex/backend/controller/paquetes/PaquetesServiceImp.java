package org.redex.backend.controller.paquetes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
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
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.PaquetesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.TipoDocumentoIdentidadRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class PaquetesServiceImp implements PaquetesService {

    @Autowired
    PaquetesRepository paquetesRepository;
    
    @Autowired
    PaisesRepository paisesRepository;
    
    @Autowired
    OficinasRepository oficinasRepository;
    
    @Autowired
    PersonaRepository personaRepository;
    
    @Autowired
    TipoDocumentoIdentidadRepository tpiRepository;
    
    @Override
    public List<Paquete> list() {
        return paquetesRepository.findAll();
    }

    @Override
    public Paquete find(Long id) {
        return paquetesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete", "id", id));
    }
    

    @Override
    @Transactional
    public CargaDatosResponse carga(MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

         Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));
         
        Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));

        Map<String, Persona> personas = personaRepository.findAll()
                .stream()
                .collect(Collectors.toMap(persona -> persona.getNumeroDocumentoIdentidad(), persona -> persona));
        
        Map<String, TipoDocumentoIdentidad> tpis = tpiRepository.findAll()
                .stream()
                .collect(Collectors.toMap(tpi -> tpi.getSimbolo(), tpi -> tpi));
        
        List<Paquete> nuevosPaquetes = new ArrayList<>();
        List<Persona> nuevasPersonas = new ArrayList<>();

        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            
            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty()) {
                    List<String> separateLine = Arrays.asList(linea.split("-"));
                    if (separateLine.size() == 22) {
                        Paquete nuevoP = leePaquete(separateLine, paises, nuevasPersonas, oficinas,personas, tpis);
                        nuevosPaquetes.add(nuevoP);
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene todos los campos");
                    }
                }
                
                nuevasPersonas.forEach((persona) -> {
                    personaRepository.save(persona);
                });
                
                nuevosPaquetes.forEach((paquete) -> {
                    paquetesRepository.save(paquete);
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }
    
    private Paquete leePaquete( List<String> datos, Map<String, Pais> mapPaises ,
            List<Persona> nuevasPersonas,Map<String, Oficina> oficinas,
            Map<String, Persona> personas, Map<String, TipoDocumentoIdentidad> tpis){
        Paquete p = new Paquete();
        
        p.setCodigoRastreo(datos.get(0));
        p.setEstado(PaqueteEstadoEnum.REGISTRADO);
        p.setFechaIngreso(ZonedDateTime.parse(datos.get(1).substring(0, 4)+"-"+
                datos.get(1).substring(4, 6)+"-"+datos.get(1).substring(6, 8)+
                "T"+datos.get(2).substring(0, 2)+datos.get(2).substring(2)));
        p.setOficinaOrigen(oficinas.get(datos.get(0).substring(0, 4)));
        p.setOficinaDestino(oficinas.get(datos.get(3)));
        Persona pO = personas.get(datos.get(12));
        if (pO == null){
            pO = new Persona();
            pO.setNombres(datos.get(4));
            pO.setPaterno(datos.get(5));
            pO.setMaterno(datos.get(6));
            pO.setTelefono(datos.get(7));
            pO.setEmail(datos.get(8));
            pO.setCelular(datos.get(9));
            pO.setPais(mapPaises.get(datos.get(10)));
            pO.setTipoDocumentoIdentidad(tpis.get(datos.get(11)));
            pO.setNumeroDocumentoIdentidad(datos.get(12));
            nuevasPersonas.add(pO);
        }
        p.setPersonaOrigen(pO);
        Persona pD = personas.get(datos.get(12));
        if (pD == null){
            pD = new Persona();
            pD.setNombres(datos.get(13));
            pD.setPaterno(datos.get(14));
            pD.setMaterno(datos.get(15));
            pD.setTelefono(datos.get(16));
            pD.setEmail(datos.get(17));
            pD.setCelular(datos.get(18));
            pD.setPais(mapPaises.get(datos.get(19)));
            pD.setTipoDocumentoIdentidad(tpis.get(datos.get(20)));
            pD.setNumeroDocumentoIdentidad(datos.get(21));
            nuevasPersonas.add(pD);
        }
        p.setPersonaDestino(pD);
        return p;
    }
    
}
