package org.redex.backend.controller.oficinas;

import java.io.IOException;
import static java.lang.Character.isDigit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.zelper.exception.AppException;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.general.Pais;
import org.redex.model.general.TipoDocumentoIdentidad;
import org.redex.model.rrhh.Colaborador;
import org.redex.model.rrhh.Oficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OficinasServiceImp implements OficinasService {

    @Autowired
    OficinasRepository oficinasRepository;

    @Autowired
    ArchivosRepository archivosRepository;

    @Autowired
    PaisesRepository paisesRepository;
    
    @Override
    public void cambiarJefe(Oficina oficina, Colaborador colaborador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void agregarColaborador(Colaborador colaborador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        //hashmap de paises por el codigo
        Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));
              
        //para guardar las oficinas que luego iran a bd
        List<Oficina> nuevasOficinas = new ArrayList<>();

        //leer el archivo y procesar el archivo
        try (Stream<String> lineas = Files.lines(filePath)) {
            List<String> lineasList = lineas.collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                // si le vas a poner validacoines aqui deberias controlarlas
                
                if (!linea.isEmpty() && isDigit(linea.charAt(0))){
                    //archivo con codigo de 3 caracteres
                    String code = linea.substring(5, 8);
                    System.out.println(code);
                    if (code.isEmpty()){
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea "+ contLinea +" no tiene pais");
                    } else {
                        nuevasOficinas.add(leerOficina(code, paises));
                    }
                }
                contLinea++;
            }
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("terminó de leer");
        //guardar cada oficina en base de datos
        for (Oficina oficina : nuevasOficinas) {
//            try {
//                oficinasRepository.save(oficina);
//                cantidadRegistros++;
//            } catch (Exception ex) {
//                cantidadErrores++;
//                errores.add("Erorr de integridad de datos");
//            }
            oficinasRepository.save(oficina);
        }

        // si hay algun error del que no se puede ignorar y se debe abortar todo en tonce spon 
        // throw new AppException("Excepcion muy mala _=(");
        // eso le mandara un respose al cliente de 500 internal server error, 
        
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    private Oficina leerOficina(String linea, Map<String, Pais> mapPaises) {
        // codigo para leer una oficina de una linea del archivo 

        Oficina oficina = new Oficina();

        oficina.setCodigo(linea);
        oficina.setPais(mapPaises.get(linea));
        oficina.setCapacidadActual(0);
        oficina.setCapacidadMaxima(100);
        oficina.setZonaHoraria(-5);
        
        return oficina;
    }

    @Override
    public List<Oficina> all() {
        return oficinasRepository.findAll();
    }

}
