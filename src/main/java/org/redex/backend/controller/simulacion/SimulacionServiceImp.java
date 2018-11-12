package org.redex.backend.controller.simulacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.*;
import org.redex.backend.model.general.Pais;
import org.redex.backend.model.simulacion.*;
import org.redex.backend.repository.*;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

@Service
@Transactional(readOnly = true)
public class SimulacionServiceImp implements SimulacionService {

    private final Logger logger = LogManager.getLogger(SimulacionServiceImp.class);

    @Autowired
    SimulacionOficinasRepository simulacionOficinasRepository;

    @Autowired
    SimulacionRepository simulacionRepository;

    @Autowired
    PaisesRepository paisesRepository;

    @Autowired
    SimulacionRuteadoService simulacionRuteadoService;

    @Autowired
    SimulacionAccionRepository accionRepository;

    @Autowired
    GestorPaquetes gestorPaquetes;

    @Autowired
    GestorVuelosAgendados gestorVuelosAgendados;

    @Autowired
    VisorSimulacion visorSimulacion;

    @Autowired
    SimulacionPaquetesRepository simulacionPaquetesRepository;

    @Override
    @Transactional
    public CargaDatosResponse cargaPaquetes(Long id, MultipartFile file) {
        Map<String, AlgoritmoOficina> oficinas = visorSimulacion.getOficinas();

        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();
        List<AlgoritmoPaquete> nuevosPaquetes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            List<String> lineasList = reader.lines().collect(Collectors.toList());
            int contLinea = 1;
            for (String linea : lineasList) {
                if (!linea.isEmpty()) {
                    List<String> separateLine = Arrays.asList(linea.split("-"));
                    if (separateLine.size() == 4) {
                        AlgoritmoPaquete nuevoP = leePaquete(separateLine, oficinas);
                        nuevosPaquetes.add(nuevoP);
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene todos los campos");
                    }
                }
            }
            gestorPaquetes.agregarLista(nuevosPaquetes);
        } catch (IOException ex) {

        }
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    @Override
    @Transactional
    public CargaDatosResponse cargaVuelos(Long id, MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        Map<String, AlgoritmoOficina> oficinas = visorSimulacion.getOficinas();

        List<AlgoritmoVuelo> nuevosVuelos = new ArrayList<>();

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
                        AlgoritmoVuelo nV = leerVuelo(codeOffice1, codeOffice2,
                                horaIni, horaFin, oficinas);
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

            gestorVuelosAgendados.setVuelos(nuevosVuelos);

        } catch (IOException ex) {

        }

        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    @Override
    @Transactional
    public CargaDatosResponse cargaOficinas(Long id, MultipartFile file) {
        Integer cantidadRegistros = 0;
        Integer cantidadErrores = 0;
        List<String> errores = new ArrayList<>();

        Map<String, AlgoritmoOficina> oficinas = visorSimulacion.getOficinas();

        oficinas.clear();

        Map<String, Pais> paises = paisesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(pais -> pais.getCodigo(), pais -> pais));


        List<AlgoritmoOficina> nuevasOficinas = new ArrayList<>();

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
                        AlgoritmoOficina oficinaNueva = leerOficina(code, paises);
                        nuevasOficinas.add(oficinaNueva);
                    }
                }
                contLinea++;
            }

            visorSimulacion.setOficinas(nuevasOficinas);

        } catch (IOException ex) {
        }

        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    private AlgoritmoPaquete leePaquete(List<String> datos, Map<String, AlgoritmoOficina> oficinas) {
        AlgoritmoPaquete p = new AlgoritmoPaquete();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(datos.get(1).substring(0, 4) + "-"
                + datos.get(1).substring(4, 6) + "-" + datos.get(1).substring(6, 8)
                + " " + datos.get(2).substring(0, 2) + datos.get(2).substring(2), formatter);
        date = date.plus(-5, ChronoUnit.HOURS);
        p.setFechaRegistro(date);
        p.setOficinaDestino(oficinas.get(datos.get(3)));
        p.setOficinaOrigen(oficinas.get(datos.get(0).substring(0, 4)));
        p.setRutaGenerada(Boolean.FALSE);
        return p;
    }

    private AlgoritmoOficina leerOficina(String linea, Map<String, Pais> mapPaises) {
        AlgoritmoOficina oficina = new AlgoritmoOficina();
        oficina.setCodigo(linea);
        oficina.setPais(mapPaises.get(linea));
        oficina.setCapacidadActual(0);
        oficina.setCapacidadMaxima(250);
        oficina.setZonaHoraria(-5);

        return oficina;
    }

    private AlgoritmoVuelo leerVuelo(String codeOffice1, String codeOffice2,
                                     String horaIni, String horaFin, Map<String, AlgoritmoOficina> mapOficinas) {
        AlgoritmoVuelo vuelo = new AlgoritmoVuelo();
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
    @Transactional
    public List<SimulacionAccion> accionesByWindow(WindowRequest request) {
        Simulacion simulacion = simulacionRepository.getOne(request.getSimulacion());
        List<SimulacionOficina> oficinas = simulacionOficinasRepository.findAllBySimulacion(simulacion);
        simulacionRuteadoService.generarVuelos(request.getInicio(), request.getFin(), simulacion);

        List<SimulacionPaquete> paquetes = simulacionPaquetesRepository.findAllBySimulacionAndFechaIngresoBetween(simulacion, request.getInicio(), request.getFin());
        for (SimulacionPaquete paquete : paquetes) {
            try {
                //simulacionRuteadoService.findRuta(paquete);
                logger.info("ruta de {} a {} generada ", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());
            } catch (PathNotFoundException ex) {
                logger.error("ruta de {} a {} no encontrada ", paquete.getOficinaOrigen().getCodigo(), paquete.getOficinaDestino().getCodigo());

            }
        }
        simulacionRuteadoService.accionesVuelosSalida(request.getInicio(), request.getFin(), simulacion);
        return accionRepository.findAllBySimulacionVentana(request.getInicio(), request.getFin(), simulacion);
    }

    @Override
    public List<SimulacionOficina> listOficinas(Long id) {
        return simulacionOficinasRepository.findAllBySimulacion(new Simulacion(id));
    }

    @Override
    @Transactional
    public void resetear(Long id) {
        Simulacion s = simulacionRepository.getOne(id);
        s.setFechaFin(null);
        simulacionRepository.save(s);
//        simulacionVueloAgendadoRepository.deleteBySimulacion(s);
        accionRepository.deleteBySimulacion(s);
    }

}
