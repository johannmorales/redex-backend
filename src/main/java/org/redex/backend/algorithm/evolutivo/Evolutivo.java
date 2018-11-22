package org.redex.backend.algorithm.evolutivo;

import com.google.common.collect.TreeMultiset;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.controller.simulacion.simulador.AvoidableException;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;

public class Evolutivo implements Algoritmo {

    private static final Logger logger = LogManager.getLogger(Evolutivo.class);

    private int iteraciones = 10;
    private int populationSize = 10;
    private double surviveRatio = 0.8;
    private double mutationRatio = 0.2;

    private int K1 = 2000;
    private int K2 = 4000;

    private static final Comparator<Cromosoma> byCost = Comparator.comparingDouble(Cromosoma::getCosto);
    private static final Supplier<TreeMultiset<Cromosoma>> supplier = () -> TreeMultiset.create(byCost);

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<VueloAgendado> run(Paquete paquete, List<VueloAgendado> vuelosTodos, List<VueloAgendado> vuelosCumplen, List<VueloAgendado> vuelosParten, List<VueloAgendado> vuelosLlegan, List<Oficina> oficinas) {

        Long t1 = System.currentTimeMillis();
        gestorAlgoritmo = new GestorAlgoritmo(vuelosCumplen, vuelosParten, vuelosLlegan, oficinas);
        Long t2 = System.currentTimeMillis();

        TreeMultiset<Cromosoma> population = initialize(paquete.getOficinaOrigen(), paquete.getOficinaDestino(), paquete.getFechaIngreso(), paquete);
        Long t3 = System.currentTimeMillis();

        for (int i = 0; i < iteraciones; i++) {
            TreeMultiset<Cromosoma> survivors = fight(population);
            TreeMultiset<Cromosoma> mutants = mutate(survivors, paquete);
            population = TreeMultiset.create(byCost);
            population.addAll(survivors);
            population.addAll(mutants);
        }

        Long t4 = System.currentTimeMillis();

//        logger.info("Gestor      {} ms", t2-t1);
//        logger.info("Inicial     {} ms", t3-t2);
//        logger.info("Iteraciones {} ms", t4-t3);


        Cromosoma winner = population.firstEntry().getElement();
        return winner.getGenes().stream().map(Gen::getVueloAgendado).collect(Collectors.toList());
    }

    private TreeMultiset<Cromosoma> initialize(Oficina ofiOrigen, Oficina ofiDestino, LocalDateTime current, Paquete paquete) {
        TreeMultiset<Cromosoma> population = TreeMultiset.create(byCost);
        for (int i = 0; i < populationSize; i++) {
            List<VueloAgendado> list = buildRandomPath(ofiOrigen, ofiDestino, current);
            if (list == null) {
                throw new AvoidableException();
            }
            LinkedList<Gen> genes = list.stream().map(va -> new Gen(va)).collect(Collectors.toCollection(() -> new LinkedList<>()));

            Cromosoma cromosome = new Cromosoma();
            cromosome.setGenes(genes);
            fitness(cromosome, paquete);
            population.add(cromosome);
        }
        return population;
    }

    private List<VueloAgendado> buildRandomPath(Oficina ofiOrigen, Oficina ofiDestino, LocalDateTime current) {
        List<VueloAgendado> posibles = gestorAlgoritmo.obtenerValidos(ofiOrigen, current);
        if (!posibles.isEmpty()) {
            Collections.shuffle(posibles);
            for (VueloAgendado va : posibles) {
                List<VueloAgendado> vas = new ArrayList<>();
                vas.add(va);
                if (va.getOficinaDestino() == ofiDestino) {
                    return vas;
                } else {
                    List<VueloAgendado> vuelosRecursivos = buildRandomPath(va.getOficinaDestino(), ofiDestino, va.getFechaFin());
                    if (vuelosRecursivos == null) {
                        continue;
                    }
                    vas.addAll(vuelosRecursivos);
                    return vas;
                }
            }
        }
        return null;
    }

    private List<VueloAgendado> buildRandomLimitedPath(Oficina ofiOrigen, Oficina ofiDestino, LocalDateTime current, LocalDateTime limit) {
        List<VueloAgendado> posibles = gestorAlgoritmo.obtenerValidos(ofiOrigen, current)
                .stream()
                .filter(va -> va.getFechaFin().isBefore(limit) || va.getFechaFin().isEqual(limit))
                .collect(Collectors.toList());

        if (!posibles.isEmpty()) {
            Collections.shuffle(posibles);

            for (VueloAgendado va : posibles) {

                List<VueloAgendado> vas = new ArrayList<>();
                vas.add(va);

                if (va.getOficinaDestino() == ofiDestino) {
                    return vas;
                } else {
                    List<VueloAgendado> vuelosRecursivos = buildRandomLimitedPath(va.getOficinaDestino(), ofiDestino, va.getFechaFin(), limit);
                    if (vuelosRecursivos == null) {
                        continue;
                    }
                    vas.addAll(vuelosRecursivos);
                    return vas;
                }
            }
        }

        return null;
    }

    private TreeMultiset<Cromosoma> fight(TreeMultiset<Cromosoma> population) {
        return population.stream().limit((int) (populationSize * surviveRatio)).collect(Collectors.toCollection(supplier));
    }

    private TreeMultiset<Cromosoma> mutate(TreeMultiset<Cromosoma> survivors, Paquete paquete) {
        TreeMultiset<Cromosoma> population = TreeMultiset.create(byCost);
        Integer nmutants = (int) (survivors.size() * mutationRatio);
        ArrayList<Cromosoma> vas = new ArrayList<>(survivors);

        ThreadLocalRandom.current().ints(0, survivors.size()).distinct().limit(nmutants).forEach(index -> {
            Cromosoma mutant = vas.get(index).copy();
            final List<Gen> genes = mutant.getGenes();

            Integer index2 = ThreadLocalRandom.current().nextInt(0, mutant.getGenes().size());
            Integer index1 = ThreadLocalRandom.current().nextInt(0, index2 + 1);

            VueloAgendado inicio = genes.get(index1).getVueloAgendado();
            VueloAgendado fin = genes.get(index2).getVueloAgendado();

            List<VueloAgendado> reemplazo = new ArrayList<>();

            if (index2 == genes.size() - 1) {
                if (index1 == 0) {
                    reemplazo = buildRandomPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), paquete.getFechaIngreso());
                } else {
                    Integer indicePrevio = index1 - 1;
                    VueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                    reemplazo = buildRandomPath(vueloPrevio.getOficinaDestino(), fin.getOficinaDestino(), vueloPrevio.getFechaInicio());
                }
            } else {
                if (index1 == 0) {
                    Integer indiceSgte = index2 + 1;
                    VueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();
                    reemplazo = buildRandomLimitedPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), paquete.getFechaIngreso(), vueloSgte.getFechaInicio());
                } else {
                    Integer indicePrevio = index1 - 1;
                    Integer indiceSgte = index2 + 1;
                    VueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                    VueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();

                    reemplazo = buildRandomLimitedPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), vueloPrevio.getFechaInicio(), vueloSgte.getFechaInicio());
                }
            }

            LinkedList<Gen> nuevos = new LinkedList<>();

            for (int i = 0; i < index1; i++) {
                nuevos.add(genes.get(i));
            }

            if (reemplazo == null) {

            } else {

                for (VueloAgendado vueloAgendado : reemplazo) {
                    Gen gen = new Gen(vueloAgendado);
                    nuevos.add(gen);
                }

                for (int i = index2 + 1; i < genes.size(); i++) {
                    nuevos.add(genes.get(i));
                }

                mutant.setGenes(nuevos);
                fitness(mutant, paquete);

                population.add(mutant);
            }
        });

        return population;
    }

    private void fitness(Cromosoma cromosome, Paquete paquete) {

        double costo = 0;

        for (Gen gen : cromosome.getGenes()) {
            VueloAgendado va = gen.getVueloAgendado();
            Integer capacidadFutura = va.getOficinaDestino().getCapacidadActual() + gestorAlgoritmo.obtenerCapacidadEnMomento(va.getOficinaDestino(), va.getFechaFin());
            double almacenPorcentaje = capacidadFutura / (double) va.getOficinaDestino().getCapacidadMaxima();

            if(capacidadFutura < 0){
                System.out.println("ERROR!!!!!!!!!!!!!!");
            }

            if (capacidadFutura != va.getOficinaDestino().getCapacidadActual()) {
                //logger.info("capacidadFutura {} , capacidadActual {} , {}", capacidadFutura, va.getOficinaDestino().getCapacidadActual(), va.getFechaFin());
            }
            costo += va.getPorcentajeUsado() * K1 + almacenPorcentaje * K2;
        }

        VueloAgendado lastFlight = cromosome.getGenes().get(cromosome.getGenes().size() - 1).getVueloAgendado();

        if (lastFlight.getOficinaDestino() != paquete.getOficinaDestino()) {
            costo = costo + 1000000;
        }

        LocalDateTime inicio = paquete.getFechaIngreso();
        LocalDateTime fin = cromosome.getGenes().get(cromosome.getGenes().size() - 1).getVueloAgendado().getFechaInicio();

        //costo += ChronoUnit.MINUTES.between(inicio, fin);


        //logger.info("\tCosto de la ruta ::: {}", costo);
        cromosome.setCosto(costo);
    }

}
