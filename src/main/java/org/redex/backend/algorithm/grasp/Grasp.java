package org.redex.backend.algorithm.grasp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Grasp implements Algoritmo {

    private static final Logger logger = LogManager.getLogger(Grasp.class);
    
    private static List<VueloAgendado> listaRuta = new ArrayList<>();
    private double porc = 0.85;//modifica el limite del fitness a elegir 0.4 - 0.85
    private double alpha = 0.5; //puede modificarse
    private int hC = 36; //cantidad de horas maximas de envios continentales
    private int hI = 48;
    private int tiempoMax;

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<VueloAgendado> run(Paquete paquete, List<VueloAgendado> vuelosAgendados, List<VueloAgendado> vuelosTerminados, List<Oficina> oficinas) {
        Integer cantidadDias = 1;
        gestorAlgoritmo = new GestorAlgoritmo(vuelosAgendados, vuelosTerminados, oficinas);

        //if (paquete.getOficinaOrigen().getPais().getContinente() == paquete.getOficinaDestino().getPais().getContinente()) {
            tiempoMax = hC * 60;
        //} else {
        //    tiempoMax = hI * 60;
        //}
        
        listaRuta = new ArrayList<>();
        //Primera parte del algoritmo
        List<VueloAgendado> lista = grasp(paquete.getOficinaOrigen(), paquete.getOficinaDestino(), paquete.getFechaIngreso(), paquete.getFechaIngreso());
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
            lista = dfs(lista.get(ind - 1).getOficinaDestino(), paquete.getOficinaDestino(), lista.get(ind - 1).getFechaFin(), paquete.getFechaIngreso());//oficina de origen , oficina destino,tiempo de envio inicial
            for (VueloAgendado va : lista) {
                listaRuta.add(va);
            }
        }
        if (lista.isEmpty()) {
            throw new PathNotFoundException(paquete); // sirve para avisarle al simulador que no se encontro ruta 
        }
        return listaRuta;
    }

//    private static void bfs(Oficina oficinaI,Oficina oficinaF,LocalDateTime tRPaquete){
//        List<VueloAgendado> frontera = new ArrayList<>(flightByOriginOffice.get(oficinaI).tailMap(tRPaquete).values());
//        List<VueloAgendado> visitados = new ArrayList<>();
//        
//        while(!frontera.isEmpty()){
//            
//            if(oficinaI == oficinaF)
//                break;
//        }
//    }
    private List<VueloAgendado> dfs(Oficina origen, Oficina destino, LocalDateTime start, LocalDateTime regP) {
//        List<VueloAgendado> listaV = new ArrayList<>(flightByOriginOffice.get(origen).tailMap(start).values());
        List<VueloAgendado> listaV = gestorAlgoritmo.obtenerValidos(origen, start);
        listaV = mejoresVuelos(listaV, origen, regP);
        for (VueloAgendado va : listaV) {
            List<VueloAgendado> list = new ArrayList<>();
            list.add(va);
            if (va.getOficinaDestino() == destino) {
                return list;
            } else {
                List<VueloAgendado> nextSteps = dfs(va.getOficinaDestino(), destino, va.getFechaFin(), regP);
                if (!nextSteps.isEmpty()) {
                    list.addAll(nextSteps);
                    return list;
                }
            }
        }
        return new ArrayList<VueloAgendado>();
    }

//    private static List<VueloAgendado> ordenxFit(List<VueloAgendado> vuelos,Oficina origen,LocalDateTime tRPaquete){
//        ArrayList<Double> listFit = new ArrayList<>();
//        List<VueloAgendado> mejoresV = new ArrayList<>();
//        if(vuelos.isEmpty()){
//            return mejoresV;//devuelve lista vacia
//        }
//        List<VueloAgendado> listA = new ArrayList<>(vuelos);
//        for(VueloAgendado va : vuelos){//remueve todos los vuelos de la lista que llegan a la oficina
//            if(va.getOficinaDestino()==origen)
//                listA.remove(va);
//        }
//        vuelos = listA;
//        for(VueloAgendado va : vuelos){
//            double fit = fitness(va,tRPaquete);
//            listFit.add(fit);
//        }
//        
//        return vuelos;
//    }
    public List<VueloAgendado> grasp(Oficina origen, Oficina destino, LocalDateTime start, LocalDateTime tRPaquete) {
        Oficina origenN;
        LocalDateTime startN;
        Random generadorAleatorios = new Random();//decidir el vuelo
        List<VueloAgendado> listaT = gestorAlgoritmo.obtenerValidos(origen, start);

        List<VueloAgendado> bestL = mejoresVuelos(listaT, origen, tRPaquete);
        while (!bestL.isEmpty()) {//mientras la lista esta llena 
            int indA = generadorAleatorios.nextInt(bestL.size());//decide el vuelo aleatoriamente
            VueloAgendado ve = bestL.get(indA);
            List<VueloAgendado> vuelosF = new ArrayList<>(gestorAlgoritmo.obtenerValidos(ve.getOficinaDestino(), ve.getFechaFin()));
            vuelosF = mejoresVuelos(vuelosF, origen, tRPaquete);
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
                bestL = mejoresVuelos(listaT, origen, tRPaquete);
                continue;
            }
            if (!grasp(origenN, destino, startN, tRPaquete).isEmpty()) {//si obtuvo solucion
                return listaRuta;
            }
            listaRuta.remove(ve);//remueve el vuelo que no sirve
            bestL.remove(ve);//remueve el veulo de la lista que no sirve
        }
        return new ArrayList<VueloAgendado>();
    }

    private List<VueloAgendado> mejoresVuelos(List<VueloAgendado> vuelos, Oficina origen, LocalDateTime tRPaquete) {
        double minF = 99999.999, maxF = -1.000;
        ArrayList<Double> listFit = new ArrayList<>();
        List<VueloAgendado> mejoresV = new ArrayList<>();
        if (!vuelos.isEmpty()) {
            List<VueloAgendado> listA = new ArrayList<>(vuelos);
            for (VueloAgendado va : vuelos) {
                if (va.getOficinaDestino() == origen) {
                    listA.remove(va);
                }
            }
            vuelos = listA;
            for (VueloAgendado va : vuelos) {
                double fit = fitness(va, tRPaquete);
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

    private double fitness(VueloAgendado vuelo, LocalDateTime tRPaquete) {//suma de porcentaje usado del avion , oficina y timepo transcurrido
       // return vuelo.getPorcentajeUsado() + vuelo.getOficinaDestino().getPorcentajeUsado() + (ChronoUnit.MINUTES.between(tRPaquete, vuelo.getFechaFin()) / tiempoMax);
        return 0;
    }

   

}
