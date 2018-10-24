package org.redex.backend.controller.persona;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.TipoDocumentoIdentidadRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.general.Archivo;
import org.redex.backend.model.general.Pais;
import org.redex.backend.model.general.Persona;
import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.redex.backend.model.seguridad.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonasServiceImp implements PersonasService {
    
    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    ArchivosRepository archivosRepository;
    
    @Autowired
    PaisesRepository paisesRepository;

    @Autowired
    TipoDocumentoIdentidadRepository tpiRepository;
    
    @Override
    public List<Persona> all() {
        return personaRepository.findAll();
    }
    
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
        
       Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));

        
        Map<String, TipoDocumentoIdentidad> tpis = tpiRepository.findAll()
                .stream()
                .collect(Collectors.toMap(tpi -> tpi.getSimbolo(), tpi -> tpi));
        
        List<Persona> nuevasPersonas = new ArrayList<>();
        
        try (Stream<String> lineas = Files.lines(filePath)) {
            
            List<String> lineasList = lineas.collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty()) {
                    List<String> separateLine = Arrays.asList(linea.split(","));
                        if (separateLine.size() == 10) {
                            Persona nuevaPersona = leePersona(separateLine, paises, tpis);
                            nuevasPersonas.add(nuevaPersona);
                        } else{
                            cantidadErrores = cantidadErrores + 1;
                            errores.add("La linea " + contLinea + " no tiene todos los campos");
                        }
                }
                for (Persona persona : nuevasPersonas) {
                    personaRepository.save(persona);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }
    
    private Persona leePersona(List<String> datos, Map<String, Pais> mapPaises,
            Map<String, TipoDocumentoIdentidad> tpis) {

        Persona p = new Persona();
        p.setNombres(datos.get(0));
        p.setPaterno(datos.get(1));
        p.setMaterno(datos.get(2));
        p.setTelefono(datos.get(3));
        p.setEmail(datos.get(4));
        p.setCelular(datos.get(5));
        p.setPais(mapPaises.get(datos.get(6)));
        p.setTipoDocumentoIdentidad(tpis.get(datos.get(7)));
        p.setNumeroDocumentoIdentidad(datos.get(8));

        return p;
    }
}
