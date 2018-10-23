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
import org.apache.commons.codec.digest.DigestUtils;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.repository.ColaboradoresRepository;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.RolesRepository;
import org.redex.backend.repository.TipoDocumentoIdentidadRepository;
import org.redex.backend.repository.UsuariosRepository;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.general.EstadoEnum;
import org.redex.model.general.Pais;
import org.redex.model.general.Persona;
import org.redex.model.general.TipoDocumentoIdentidad;
import org.redex.model.rrhh.CargoEnum;
import org.redex.model.rrhh.Colaborador;
import org.redex.model.rrhh.Oficina;
import org.redex.model.seguridad.Rol;
import org.redex.model.seguridad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuariosServiceImp implements UsuariosService {

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

    @Autowired
    TipoDocumentoIdentidadRepository tpiRepository;

    @Autowired
    RolesRepository rolesRepository;

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

        Map<String, Rol> roles = rolesRepository.findAll()
                .stream()
<<<<<<< HEAD
                .collect(Collectors.toMap(rol -> rol.getCodigo().name(), rol -> rol));
        
=======
                .collect(Collectors.toMap(rol -> rol.getNombre(), rol -> rol));

>>>>>>> master
        Map<String, TipoDocumentoIdentidad> tpis = tpiRepository.findAll()
                .stream()
                .collect(Collectors.toMap(tpi -> tpi.getSimbolo(), tpi -> tpi));

        Map<String, Oficina> oficinas = oficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));

        //para guardar las oficinas que luego iran a bd
        List<Persona> nuevasPersonas = new ArrayList<>();
        List<Colaborador> nuevosColaboradores = new ArrayList<>();
        List<Usuario> nuevosUsuarios = new ArrayList<>();

        try (Stream<String> lineas = Files.lines(filePath)) {
            List<String> lineasList = lineas.collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                // si le vas a poner validacoines aqui deberias controlarlas
                if (!linea.isEmpty()) {

                    List<String> separateLine = Arrays.asList(linea.split(","));
                    if (separateLine.size() == 14) {
                        Persona nuevaPersona = leePersona(separateLine, paises, tpis);
                        Colaborador nuevoColaborador = leeColaborador(separateLine, nuevaPersona, oficinas);
                        Usuario nuevoUsuario = leeUsuario(separateLine, nuevoColaborador, roles);
                        nuevasPersonas.add(nuevaPersona);
                        nuevosColaboradores.add(nuevoColaborador);
                        nuevosUsuarios.add(nuevoUsuario);
                    }

                }
            }

            for (Persona persona : nuevasPersonas) {
                personaRepository.save(persona);
            }

            for (Colaborador colaborador : nuevosColaboradores) {
                colaboradoresRepository.save(colaborador);
            }

            for (Usuario usuario : nuevosUsuarios) {
                usuariosRepository.save(usuario);
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

    private Colaborador leeColaborador(List<String> datos, Persona p,
            Map<String, Oficina> oficinas) {
        Colaborador c = new Colaborador();
        c.setPersona(p);
        c.setCargo(CargoEnum.valueOf(datos.get(9)));
        c.setEstado(EstadoEnum.valueOf(datos.get(10)));
        c.setOficina(oficinas.get(datos.get(11)));
        c.setCelular(p.getCelular());
        c.setEmail(p.getEmail());
        c.setTelefono(p.getTelefono());

        return c;
    }

    private Usuario leeUsuario(List<String> datos, Colaborador c, Map<String, Rol> roles) {
        Usuario u = new Usuario();
        u.setColaborador(c);
        u.setEstado(EstadoEnum.ACTIVO);
        u.setUsername(datos.get(12));
        String password = DigestUtils.sha256Hex(datos.get(13));
        u.setPassword(password);
        System.out.println(datos.get(9));
        System.out.println(roles.get(datos.get(9)));
        u.setRol(roles.get(datos.get(9)));
        return u;
    }

    @Override
    public List<Usuario> all() {
        return usuariosRepository.findAll();
    }

    @Transactional
    public void crearUsuario(UsuariosPayload usuario) {

    }

    @Transactional
    public void activar(UsuariosPayload usuario) {
    }

    @Transactional
    public void desactivar(UsuariosPayload usuario) {

    }

    @Transactional
    public void restablecerContraseña(UsuariosPayload usuario) {

    }

}
