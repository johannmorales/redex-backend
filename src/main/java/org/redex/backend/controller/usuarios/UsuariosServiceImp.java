/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redex.backend.controller.usuarios;

import java.io.IOException;
import static java.lang.Character.isDigit;
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
import org.redex.backend.repository.ColaboradoresRepository;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.UsuariosRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.general.Pais;
import org.redex.model.general.Persona;
import org.redex.model.rrhh.Colaborador;
import org.redex.model.rrhh.Oficina;
import org.redex.model.seguridad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oscar
 */
@Service
@Transactional(readOnly = true)
public class UsuariosServiceImp implements UsuariosService{
    @Autowired
    OficinasRepository oficinasRepository;

    @Autowired
    ArchivosRepository archivosRepository;

    @Autowired
    PaisesRepository paisesRepository;
    
    @Autowired
    PersonaRepository personaRepository;
    
    @Autowired
    ColaboradoresRepository colaboradoresRepository;
    
    @Autowired
    UsuariosRepository usuariosRepository;
    
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

        //para guardar las oficinas que luego iran a bd
        List<Persona> nuevasPersonas = new ArrayList<>();
        List<Colaborador> nuevosColaboradores = new ArrayList<>();
        List<Usuario> nuevosUsuarios = new ArrayList<>();
        
        try (Stream<String> lineas = Files.lines(filePath)) {
            List<String> lineasList = lineas.collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                // si le vas a poner validacoines aqui deberias controlarlas
                 if (!linea.isEmpty()){
                     List<String> separateLine = Arrays.asList(linea.split(","));
                     Persona nuevaPersona = leePersona();
                     Colaborador nuevoColaborador = leeColaborador();
                     Usuario nuevoUsuario = leeUsuario();
                     nuevasPersonas.add(nuevaPersona);
                     nuevosColaboradores.add(nuevoColaborador);
                     nuevosUsuarios.add(nuevoUsuario);
                 }
            }
            
            for (Persona persona: nuevasPersonas){
                personaRepository.save(persona);
            }
            
            for (Colaborador colaborador: nuevosColaboradores){
                colaboradoresRepository.save(colaborador);
            }
            
            for (Usuario usuario: nuevosUsuarios){
                usuariosRepository.save(usuario);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }
    
    private Persona leePersona(){
        Persona p = new Persona();
        return p;
    }
    
    private Colaborador leeColaborador(){
        Colaborador c = new Colaborador();
        return c;
    }
    
    private Usuario leeUsuario(){
        Usuario u = new Usuario();
        return u;
    }
}
