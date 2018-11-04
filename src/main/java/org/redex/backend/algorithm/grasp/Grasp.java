package org.redex.backend.algorithm.grasp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoPaquete;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public class Grasp implements Algoritmo {

    private static final Logger logger = LogManager.getLogger(Grasp.class);
    
    private static List<AlgoritmoVueloAgendado> listaRuta = new ArrayList<>();
    private double porc = 0.85;//modifica el limite del fitness a elegir 0.4 - 0.85
    private double alpha = 0.5; //puede modificarse
    private int hC = 36; //cantidad de horas maximas de envios continentales
    private int hI = 48;
    private int tiempoMax;

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<AlgoritmoVueloAgendado> run(AlgoritmoPaquete paquete, List<AlgoritmoVueloAgendado> vuelosAgendados, List<AlgoritmoVueloAgendado> vuelosTerminados, List<AlgoritmoOficina> oficinas) {
        Integer cantidadDias = 1;
        gestorAlgoritmo = new GestorAlgoritmo(vuelosAgendados, vuelosTerminados, oficinas);

        //if (paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()) {
            tiempoMax = hC * 60;
        //} else {
        //    tiempoMax = hI * 60;
        //}
        
        listaRuta = new ArrayList<>();
        //Primera parte del algoritmo
        List<AlgoritmoVueloAgendado> lista = grasp(paquete.getOficinaOrigen(), paquete.getOficinaDestino(), paquete.getFechaRegistro(), paquete.getFechaRegistro());
        //decidir el alpha que sera el LIMITE de la ruta 
        int numE = lista.size();//devuelve tamaño de ruta
        int ind = (int) (numE * alpha);
        if (ind > 0) {//si llega a mas de 2 vuelos
            //remueve de la pos numE/alpha hasta el destino
            for (int i = 0; i < numE - ind; i++) {
                lista.remove(ind);
            }
            listaRuta = lista;
            //con bfs se calcula el resto de la ruta
            lista = dfs(lista.get(ind - 1).getOficinaDestino(), paquete.getOficinaDestino(), lista.get(ind - 1).getFechaFin(), paquete.getFechaRegistro());//oficina de origen , oficina destino,tiempo de envio inicial
            for (AlgoritmoVueloAgendado va : lista) {
                listaRuta.add(va);
            }
        }
        if (lista.isEmpty()) {
            throw new PathNotFoundException(paquete); // sirve para avisarle al simulador que no se encontro ruta 
        }
        return listaRuta;
    }

//    private static void bfs(AlgoritmoOficina oficinaI,AlgoritmoOficina oficinaF,LocalDateTime tRAlgoritmoPaquete){
//        List<AlgoritmoVueloAgendado> frontera = new ArrayList<>(flightByOriginOffice.get(oficinaI).tailMap(tRAlgoritmoPaquete).values());
//        List<AlgoritmoVueloAgendado> visitados = new ArrayList<>();
//        
//        while(!frontera.isEmpty()){
//            
//            if(oficinaI == oficinaF)
//                break;
//        }
//    }
    private List<AlgoritmoVueloAgendado> dfs(AlgoritmoOficina origen, AlgoritmoOficina destino, LocalDateTime start, LocalDateTime regP) {
//        List<AlgoritmoVueloAgendado> listaV = new ArrayList<>(flightByOriginOffice.get(origen).tailMap(start).values());
        List<AlgoritmoVueloAgendado> listaV = gestorAlgoritmo.obtenerValidos(origen, start);
        listaV = mejoresVuelos(listaV, origen, regP);
        for (AlgoritmoVueloAgendado va : listaV) {
            List<AlgoritmoVueloAgendado> list = new ArrayList<>();
            list.add(va);
            if (va.getOficinaDestino() == destino) {
                return list;
            } else {
                List<AlgoritmoVueloAgendado> nextSteps = dfs(va.getOficinaDestino(), destino, va.getFechaFin(), regP);
                if (!nextSteps.isEmpty()) {
                    list.addAll(nextSteps);
                    return list;
                }
            }
        }
        return new ArrayList<AlgoritmoVueloAgendado>();
    }

//    private static List<AlgoritmoVueloAgendado> ordenxFit(List<AlgoritmoVueloAgendado> vuelos,AlgoritmoOficina origen,LocalDateTime tRAlgoritmoPaquete){
//        ArrayList<Double> listFit = new ArrayList<>();
//        List<AlgoritmoVueloAgendado> mejoresV = new ArrayList<>();
//        if(vuelos.isEmpty()){
//            return mejoresV;//devuelve lista vacia
//        }
//        List<AlgoritmoVueloAgendado> listA = new ArrayList<>(vuelos);
//        for(AlgoritmoVueloAgendado va : vuelos){//remueve todos los vuelos de la lista que llegan a la oficina
//            if(va.getOficinaDestino()==origen)
//                listA.remove(va);
//        }
//        vuelos = listA;
//        for(AlgoritmoVueloAgendado va : vuelos){
//            double fit = fitness(va,tRAlgoritmoPaquete);
//            listFit.add(fit);
//        }
//        
//        return vuelos;
//    }
    public List<AlgoritmoVueloAgendado> grasp(AlgoritmoOficina origen, AlgoritmoOficina destino, LocalDateTime start, LocalDateTime tRAlgoritmoPaquete) {
        AlgoritmoOficina origenN;
        LocalDateTime startN;
        Random generadorAleatorios = new Random();//decidir el vuelo
        List<AlgoritmoVueloAgendado> listaT = gestorAlgoritmo.obtenerValidos(origen, start);

        List<AlgoritmoVueloAgendado> bestL = mejoresVuelos(listaT, origen, tRAlgoritmoPaquete);
        while (!bestL.isEmpty()) {//mientras la lista esta llena 
            int indA = generadorAleatorios.nextInt(bestL.size());//decide el vuelo aleatoriamente
            AlgoritmoVueloAgendado ve = bestL.get(indA);
            List<AlgoritmoVueloAgendado> vuelosF = new ArrayList<>(gestorAlgoritmo.obtenerValidos(ve.getOficinaDestino(), ve.getFechaFin()));
            vuelosF = mejoresVuelos(vuelosF, origen, tRAlgoritmoPaquete);
            LocalDateTime lastStatus;
            Integer variacion;
            try {
                variacion = gestorAlgoritmo.obtenerCapacidadEnMomento(ve.getOficinaDestino(), ve.getFechaFin());
            } catch (Exception ex) {
                variacion = 0;
            }
            variacion += ve.getOficinaDestino().getCapacidadActual();
            if ((variacion <= ve.getOficinaDestino().getCapacidadMaxima()) || !vuelosF.isEmpty()) {//si existe capacidad en la oficina de llegada o su lista de vuelos no es vacia
                listaRuta.add(ve);//añade vuelo elegido
                origenN = ve.getOficinaDestino();
                startN = ve.getFechaFin();
                if ((ve.getOficinaDestino() == destino))//si añadiendo ese vuelo ya llegue a destino
                {
                    return listaRuta;
                }
            } else {//si no hay mas vuelos hacia destino o la capacidad sobrepasa el limite
                bestL.remove(ve);
                listaT.remove(ve);
                bestL = mejoresVuelos(listaT, origen, tRAlgoritmoPaquete);
                continue;
            }
            if (!grasp(origenN, destino, startN, tRAlgoritmoPaquete).isEmpty()) {//si obtuvo solucion
                return listaRuta;
            }
            listaRuta.remove(ve);//remueve el vuelo que no sirve
            bestL.remove(ve);//remueve el veulo de la lista que no sirve
        }
        return new ArrayList<AlgoritmoVueloAgendado>();
    }

    private List<AlgoritmoVueloAgendado> mejoresVuelos(List<AlgoritmoVueloAgendado> vuelos, AlgoritmoOficina origen, LocalDateTime tRAlgoritmoPaquete) {
        double minF = 99999.999, maxF = -1.000;
        ArrayList<Double> listFit = new ArrayList<>();
        List<AlgoritmoVueloAgendado> mejoresV = new ArrayList<>();
        if (!vuelos.isEmpty()) {
            List<AlgoritmoVueloAgendado> listA = new ArrayList<>(vuelos);
            for (AlgoritmoVueloAgendado va : vuelos) {
                if (va.getOficinaDestino() == origen) {
                    listA.remove(va);
                }
            }
            vuelos = listA;
            for (AlgoritmoVueloAgendado va : vuelos) {
                double fit = fitness(va, tRAlgoritmoPaquete);
                if (fit < minF) {
                    minF = fit;
                }
                if (maxF < fit) {
                    maxF = fit;
                }
                listFit.add(fit);
            }
            double fitOpt = minF + porc * (maxF - minF);//limite optimo para los vuelos
            int cant = 0;
            if (!vuelos.isEmpty()) {
                for (double fit : listFit) {
                    if (fit <= fitOpt) {
                        mejoresV.add(vuelos.get(cant));
                    }
                    cant++;
                }
            }
        }
        return mejoresV;
    }

    private double fitness(AlgoritmoVueloAgendado vuelo, LocalDateTime tRAlgoritmoPaquete) {//suma de porcentaje usado del avion , oficina y timepo transcurrido
       // return vuelo.getPorcentajeUsado() + vuelo.getOficinaDestino().getPorcentajeUsado() + (ChronoUnit.MINUTES.between(tRAlgoritmoPaquete, vuelo.getFechaFin()) / tiempoMax);
        return 0;
    }

    @Override
    public List<SimulacionVueloAgendado> runBatch(List<SimulacionPaquete> paquetes, List<VueloAgendado> vuelosAgendados, List<SimulacionOficina> oficinas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
