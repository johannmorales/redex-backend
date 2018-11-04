package org.redex.backend.controller.simulacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Character.isDigit;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.redex.backend.controller.oficinas.OficinasServiceImp;
import org.redex.backend.model.general.Pais;
import org.springframework.beans.factory.annotation.Autowired;
import org.redex.backend.model.simulacion.Simulacion;
import org.redex.backend.model.simulacion.SimulacionAccion;
import org.redex.backend.model.simulacion.SimulacionEstadoEnum;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVuelo;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.SimulacionOficinasRepository;
import org.redex.backend.repository.SimulacionPaquetesRepository;
import org.redex.backend.repository.SimulacionRepository;
import org.redex.backend.repository.SimulacionVueloAgendadoRepository;
import org.redex.backend.repository.SimulacionVuelosRepository;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SimulacionServiceImp implements SimulacionService {

    @Autowired
    SimulacionOficinasRepository simulacionOficinasRepository;

    @Autowired
    SimulacionRepository simulacionRepository;

    @Autowired
    SimulacionVuelosRepository vuelosRepository;

    @Autowired
    PaisesRepository paisesRepository;

    @Autowired
    SimulacionPaquetesRepository simulacionPaquetesRepository;

    @Autowired
    SimulacionVueloAgendadoRepository simulacionVueloAgendadoRepository;

    @Override
    @Transactional
    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file) {
        Simulacion simu = simulacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Simulacion", "id", id));

        //simuPaquete.setSimulacion(simu)
        Map<String, SimulacionOficina> oficinas = simulacionOficinasRepository.findAllBySimulacion(simu)
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));

        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();
        List<SimulacionPaquete> nuevosPaquetes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {

            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty()) {
                    List<String> separateLine = Arrays.asList(linea.split("-"));
                    if (separateLine.size() == 4) {
                        SimulacionPaquete nuevoP = leePaquete(separateLine, oficinas);
                        nuevoP.setSimulacion(simu);
                        nuevosPaquetes.add(nuevoP);
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene todos los campos");
                    }
                }
            }
            nuevosPaquetes.forEach((paquete) -> {
                simulacionPaquetesRepository.save(paquete);
            });
        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);

    }

    @Override
    @Transactional
    public CargaDatosResponse cargaVuelos(Long id, MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        Map<String, SimulacionOficina> oficinas = simulacionOficinasRepository.findAll()
                .stream()
                .collect(Collectors.toMap(oficina -> oficina.getCodigo(), oficina -> oficina));

        Simulacion simu = simulacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Simulacion", "id", id));
        List<SimulacionVuelo> nuevosVuelos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {

            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
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
                        SimulacionVuelo nV = leerVuelo(codeOffice1, codeOffice2,
                                horaIni, horaFin, oficinas);
                        nV.setSimulacion(simu);
                        nuevosVuelos.add(nV);
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
            nuevosVuelos.forEach((vuelo) -> {
                vuelosRepository.save(vuelo);
            });

        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    @Override
    @Transactional
    public CargaDatosResponse cargaOficinas(Long id, MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        Simulacion simu = simulacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Simulacion", "id", id));

        Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));

        List<SimulacionOficina> nuevasOficinas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {

            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty() && isDigit(linea.charAt(0))) {
                    String code = linea.substring(5, 9);
                    if (code.isEmpty()) {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene pais");
                    } else {
                        SimulacionOficina oficinaNueva = leerOficina(code, paises);
                        oficinaNueva.setSimulacion(simu);
                        nuevasOficinas.add(oficinaNueva);
                    }
                }
                contLinea++;
            }
            nuevasOficinas.forEach((oficina) -> {
                simulacionOficinasRepository.save(oficina);
            });

        } catch (IOException ex) {
            Logger.getLogger(OficinasServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    private SimulacionPaquete leePaquete(List<String> datos, Map<String, SimulacionOficina> oficinas) {
        SimulacionPaquete p = new SimulacionPaquete();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(datos.get(1).substring(0, 4) + "-"
                + datos.get(1).substring(4, 6) + "-" + datos.get(1).substring(6, 8)
                + " " + datos.get(2).substring(0, 2) + datos.get(2).substring(2), formatter);
        date = date.plus(-5, ChronoUnit.HOURS);
        p.setFechaIngreso(date);
        p.setOficinaDestino(oficinas.get(datos.get(3)));
        p.setOficinaOrigen(oficinas.get(datos.get(0).substring(0, 4)));
        return p;
    }

    private SimulacionOficina leerOficina(String linea, Map<String, Pais> mapPaises) {
        SimulacionOficina oficina = new SimulacionOficina();
        oficina.setCodigo(linea);
        oficina.setPais(mapPaises.get(linea));
        oficina.setCapacidadInicial(0);
        oficina.setCapacidadMaxima(100);
        oficina.setZonaHoraria(-5);

        return oficina;
    }

    private SimulacionVuelo leerVuelo(String codeOffice1, String codeOffice2,
            String horaIni, String horaFin, Map<String, SimulacionOficina> mapOficinas) {
        // codigo para leer una oficina de una linea del archivo 

        SimulacionVuelo vuelo = new SimulacionVuelo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        vuelo.setOficinaOrigen(mapOficinas.get(codeOffice1));
        vuelo.setOficinaDestino(mapOficinas.get(codeOffice2));
        vuelo.setHoraInicio(LocalTime.parse(horaIni, dateTimeFormatter));
        vuelo.setHoraFin(LocalTime.parse(horaFin, dateTimeFormatter));
        vuelo.setCapacidad(500);

        return vuelo;
    }

    @Override
    public Simulacion crear() {
        Simulacion s = new Simulacion();
        s.setCantidadOficinas(0);
        s.setCantidadPaquetes(0);
        s.setEstado(SimulacionEstadoEnum.INTEGRANDO);
        s.setFechaFin(null);
        s.setFechaInicio(null);

        return s;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<Simulacion> crimsonList(CrimsonTableRequest request) {
        return Page.empty();
    }

    @Override
    public List<SimulacionAccion> accionesByWindow(WindowRequest request) {
        Simulacion simulacion = simulacionRepository.getOne(request.getSimulacion());

        List<SimulacionPaquete> paquetes = simulacionPaquetesRepository.findAllBySimulacionAndFechaIngresoBetween(simulacion, request.getInicio(), request.getFin());
        
        List<SimulacionVueloAgendado> vuelosAgendados = simulacionVueloAgendadoRepository.findAllBySimulacionAndFechaInicioBetween(simulacion, request.getInicio(), request.getFin());
        
        List<SimulacionAccion> acciones = new ArrayList<>();
        
        for (SimulacionVueloAgendado va : vuelosAgendados) {
            acciones.add(SimulacionAccion.of(va));
        }
        
        
        
        Collections.sort(acciones, Comparator.comparing(SimulacionAccion::getFechaInicio));
        
        return acciones;
    }

}
