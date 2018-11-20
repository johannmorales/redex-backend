package org.redex.backend.controller.paquetes;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.redex.backend.algorithm.AlgoritmoWrapper;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.PaqueteEstadoEnum;
import org.redex.backend.model.envios.PaqueteRuta;
import org.redex.backend.model.envios.RutaEstadoEnum;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.general.Pais;
import org.redex.backend.model.general.Persona;
import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.repository.OficinasRepository;
import org.redex.backend.repository.PaisesRepository;
import org.redex.backend.repository.PaqueteRutaRepository;
import org.redex.backend.repository.PaquetesRepository;
import org.redex.backend.repository.PersonaRepository;
import org.redex.backend.repository.TipoDocumentoIdentidadRepository;
import org.redex.backend.repository.VuelosAgendadosRepository;
import org.redex.backend.repository.VuelosRepository;
import org.redex.backend.security.DataSession;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.redex.backend.zelper.exception.ResourceNotFoundException;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.albatross.zelpers.miscelanea.JsonHelper;

@Service
@Transactional(readOnly = true)
public class PaquetesServiceImp implements PaquetesService {

    private static final Logger logger = LogManager.getLogger(PaquetesServiceImp.class);

    @Autowired
    PaquetesRepository paquetesRepository;

    @Autowired
    PaisesRepository paisesRepository;

    @Autowired
    OficinasRepository oficinasRepository;

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    VuelosRepository vuelosRepository;

    @Autowired
    TipoDocumentoIdentidadRepository tpiRepository;

    @Autowired
    VuelosAgendadosRepository vuelosAgendadosRepository;

    @Autowired
    PaqueteRutaRepository paqueteRutaRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;
    
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
                        Paquete nuevoP = leePaquete(separateLine, paises, nuevasPersonas, oficinas, personas, tpis);
                        nuevosPaquetes.add(nuevoP);
                    } else {
                        cantidadErrores = cantidadErrores + 1;
                        errores.add("La linea " + contLinea + " no tiene todos los campos");
                    }
                }

                nuevosPaquetes.forEach((paquete) -> {
                    System.out.println(paquete.getPersonaOrigen().getId());
                    System.out.println(paquete.getPersonaDestino().getId());
                    paquetesRepository.save(paquete);
                });
            }
        } catch (IOException ex) {
        }
        return new CargaDatosResponse(cantidadErrores, cantidadRegistros, "Carga finalizada con exito", errores);
    }

    private Paquete leePaquete(List<String> datos, Map<String, Pais> mapPaises,
            List<Persona> nuevasPersonas, Map<String, Oficina> oficinas,
            Map<String, Persona> personas, Map<String, TipoDocumentoIdentidad> tpis) {
        Paquete p = new Paquete();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(datos.get(1).substring(0, 4) + "-"
                + datos.get(1).substring(4, 6) + "-" + datos.get(1).substring(6, 8)
                + " " + datos.get(2).substring(0, 2) + datos.get(2).substring(2), formatter);
        date = date.plus(-5, ChronoUnit.HOURS);
        System.out.println(datos.get(0).substring(0, 4));

        p.setCodigoRastreo(datos.get(0));
        p.setEstado(PaqueteEstadoEnum.REGISTRADO);
        p.setFechaIngreso(ZonedDateTime.of(date, ZoneId.of("UTC")).toLocalDateTime());
        p.setOficinaDestino(oficinas.get(datos.get(3)));
        p.setOficinaOrigen(oficinas.get(datos.get(0).substring(0, 4)));
        Persona pO = personas.get(datos.get(12));
        if (pO == null) {
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
            personaRepository.save(pO);
            System.out.println("creando nuevo cliente ID:" + pO.getId());
        }
        p.setPersonaOrigen(pO);
        Persona pD = personas.get(datos.get(21));
        if (pD == null) {
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
            personaRepository.save(pD);
            System.out.println("creando nuevo cliente ID:" + pD.getId());
        }
        p.setPersonaDestino(pD);
        return p;
    }

    @Override
    @Transactional
    public void save(Paquete paquete) {
        paquete.setCodigoRastreo(String.format("%09d", System.currentTimeMillis()));
        paquete.setEstado(PaqueteEstadoEnum.REGISTRADO);
        paquete.setFechaIngreso(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        Oficina oo = oficinasRepository.findById(paquete.getOficinaOrigen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Paquete", "id", paquete.getOficinaOrigen().getId()));

        Oficina od = oficinasRepository.findById(paquete.getOficinaDestino().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Paquete", "id", paquete.getOficinaDestino().getId()));

        paquete.setOficinaDestino(od);
        paquete.setOficinaOrigen(oo);

        paquetesRepository.save(paquete);

        this.generarRuta(paquete);
    }

    @Override
    public Page<Paquete> crimsonList(CrimsonTableRequest request, DataSession ds) {
        
        return paquetesRepository.crimsonList(request.getSearch(), request.createPagination(),ds.getOficina().getId());

//        switch (ds.getRol().getCodigo()){
//            case ADMINISTRADOR:
//            case GERENTE_GENERAL:
//                return paquetesRepository.crimsonList(request.getSearch(), request.createPagination());
//            case EMPLEADO:
//            case JEFE_OFICINA:
//                return paquetesRepository.crimsonListByOficina(request.getSearch(), ds.getOficina(), request.createPagination());
//            default:
//                return null;
//        }
    }

    
    
    public void generarRuta(Paquete p) {

        List<Oficina> oficinas = oficinasRepository.findAll();

        LocalDateTime fechaInicio = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime fechaFin = fechaInicio.plusHours(24L);

        if (p.esIntercontinental()) {
            fechaFin = fechaFin.plusHours(24L);
        }

        List<VueloAgendado> vuelosAgendados = vuelosAgendadosRepository.findAllAlgoritmo(fechaInicio, fechaFin);

        List<VueloAgendado> vuelosTerminados = vuelosAgendadosRepository.findAllTerminados(fechaInicio, fechaFin);

        Evolutivo e = new Evolutivo();
        List<VueloAgendado> va = e.run(p, vuelosAgendados, vuelosTerminados, oficinas);

        int aux = 0;
        for (VueloAgendado vva : va) {
            PaqueteRuta pR = new PaqueteRuta();
            pR.setPaquete(p);
            pR.setVueloAgendado(vva);
            if (aux == 0) {
                pR.setEstado(RutaEstadoEnum.ACTIVO);
            } else {
                pR.setEstado(RutaEstadoEnum.PENDIENTE);
            }
            pR.setOrden(aux);
            aux++;
            paqueteRutaRepository.save(pR);
        }
        p.setFechaSalida(va.get(aux - 1).getFechaFin());
        paquetesRepository.save(p);
    }
    
    @Override
    public ObjectNode estadoPaquete(String trackNum){
        
        EntityManager em = emf.createEntityManager();
        TrackReport response = new TrackReport();
        
        String q2 = "Select estado from paquete where codigo_rastreo='"+ trackNum+"'";
        List<Object[]> paquete = (List<Object[]>)em.createNativeQuery(q2)
                              .getResultList();
        Iterator it2 = paquete.iterator();
        
        if (!it2.hasNext()){
            ObjectNode trackingJson = JsonHelper.createJson(response, JsonNodeFactory.instance, new String[]{
            "status"
             });
            return trackingJson;
        }
        String eActual = "";
        while(it2.hasNext()){
            eActual = (String)it2.next();
        }
        
        String q = "SELECT pr.orden,pr.estado, va.fecha_inicio, va.fecha_fin, pa.nombre as nI ,pa.latitud as laI, pa.longitud as loI, pa2.nombre as nF, pa.latitud as laF,pa.longitud as loF " +
            "FROM redex.paquete_ruta pr, paquete p, vuelo_agendado va, vuelo v, " +
            "oficina o, pais pa, oficina o2, pais pa2 " +
            "where p.codigo_rastreo ='"+trackNum+"' " +
            "and p.id = pr.id_paquete and pr.id_vuelo_agendado = va.id and " +
            "va.id_vuelo= v.id and v.id_oficina_origen = o.id and o.id_pais = pa.id " +
            "and v.id_oficina_destino= o2.id and o2.id_pais =pa2.id";
        
        
        
        
        List<Object[]> arr_cust = (List<Object[]>)em.createNativeQuery(q)
                              .getResultList();
        
        
        Iterator it = arr_cust.iterator();
        if (!it.hasNext()){
            response.setStatus(0);
            ObjectNode trackingJson = JsonHelper.createJson(response, JsonNodeFactory.instance, new String[]{
            "status"
             });
            return trackingJson;
        }
        List<PackageRoute> tr = new ArrayList<PackageRoute>();
        int firstActive = -1;
        int cont = 0;
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            PackageRoute tAux = new PackageRoute();
            tAux.setOrden((int)obj[0]);
            tAux.setEstado((String)obj[1]);
            Date date = new Date(((Timestamp)obj[2]).getTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            tAux.setFechaInicio(strDate);
            Date dated = new Date(((Timestamp)obj[3]).getTime());
            String strDateF = dateFormat.format(dated);
            tAux.setFechaFin(strDateF);
            tAux.setPaisI((String)obj[4]);
            tAux.setLatI(((BigDecimal)obj[5]));
            tAux.setLonI(((BigDecimal)obj[6]));
            tAux.setPaisF((String)obj[7]);
            tAux.setLatF(((BigDecimal)obj[8]));
            tAux.setLonF(((BigDecimal)obj[9]));
            if(firstActive == -1 && tAux.getEstado().equals("ACTIVO")){
                firstActive = cont;
            }
            tr.add(tAux);
            cont++;
        }
        response.setPlan(tr);
        PackageRoute actual = tr.get(firstActive);
        response.setStatus(1);
        response.setEstado(eActual);
        response.setDestino(actual.getPaisF());
        response.setOrigen(actual.getPaisI());
        if(eActual.equals("EN_VUELO")){
            response.setLocalizacion(eActual);
        } else {
            response.setLocalizacion(actual.getPaisI());
        }
        
        ObjectNode trackingJson = JsonHelper.createJson(response, JsonNodeFactory.instance, new String[]{
            "status",
            "estado",
            "origen",
            "destino",
            "localizacion",
            "plan.*"
            
        });
        
        return trackingJson;
    }
}
