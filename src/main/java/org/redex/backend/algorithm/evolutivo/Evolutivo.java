package org.redex.backend.algorithm.evolutivo;

import com.google.common.collect.TreeMultiset;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.Algoritmo;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.controller.simulacion.simulador.AvoidableException;
import org.redex.backend.controller.simulacion.simulador.DeadSimulationException;
import org.redex.backend.controller.simulacion.simulador.SortedSimpleList;
import org.redex.backend.controller.simulacion.simulador.SortedSumList;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import pe.albatross.zelpers.miscelanea.Assert;

public class Evolutivo implements Algoritmo {

    private static final Logger logger = LogManager.getLogger(Evolutivo.class);

    private static int MAX_DEPTH = 3;

    private int iteraciones = 20;
    private int populationSize = 10;
    private double surviveRatio = 0.8;
    private double mutationRatio = 0.2;

    private int K1 = 69;
    private int K2 = 420;

    private static final Comparator<Cromosoma> byCost = Comparator.comparingDouble(Cromosoma::getCosto);
    private static final Supplier<TreeMultiset<Cromosoma>> supplier = () -> TreeMultiset.create(byCost);

    private GestorAlgoritmo gestorAlgoritmo;

    private SortedSimpleList<Double, List<VueloAgendado>> population;

    public List<VueloAgendado> run(
            Paquete paquete,
            List<VueloAgendado> vuelosCumplen,
            HashMap<Oficina, SortedSumList<LocalDateTime, Integer>> movsPositivos,
            HashMap<Oficina, SortedSumList<LocalDateTime, Integer>> movsNegativos,
            List<Oficina> oficinas) {
        gestorAlgoritmo = new GestorAlgoritmo(vuelosCumplen, movsPositivos, movsNegativos, oficinas);
        return procesarAlgoritmo(paquete);
    }

    @Override
    public List<VueloAgendado> run(
            Paquete paquete,
            List<VueloAgendado> vuelosTodos,
            List<VueloAgendado> vuelosCumplen,
            List<VueloAgendado> vuelosParten,
            List<VueloAgendado> vuelosLlegan,
            List<Oficina> oficinas) {
        gestorAlgoritmo = new GestorAlgoritmo(vuelosCumplen, vuelosParten, vuelosLlegan, oficinas);
        return procesarAlgoritmo(paquete);
    }

    private TreeMultiset<Cromosoma> initialize(Oficina ofiOrigen, Oficina ofiDestino, LocalDateTime current, Paquete paquete) {
        TreeMultiset<Cromosoma> population = TreeMultiset.create(byCost);

        Collection<Cromosoma> syncList = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Thread thread = new Thread(() -> {
                List<VueloAgendado> list = buildRandomPath(ofiOrigen, ofiDestino, current, 0);
                if (list != null) {
                    LinkedList<Gen> genes = list.stream().map(va -> new Gen(va)).collect(Collectors.toCollection(() -> new LinkedList<>()));
                    Cromosoma cromosome = new Cromosoma();
                    cromosome.setGenes(genes);
                    fitness(cromosome, paquete);
                    syncList.add(cromosome);
                }
            });
            threads.add(thread);
            thread.run();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Cromosoma cromosoma : syncList) {
            population.add(cromosoma);
        }

        return population;
    }

    private List<VueloAgendado> procesarAlgoritmo(Paquete paquete) {
        TreeMultiset<Cromosoma> population = initialize(paquete.getOficinaOrigen(), paquete.getOficinaDestino(), paquete.getFechaIngreso(), paquete);

        if (population.isEmpty()) {
            throw new AvoidableException();
        }

        for (int i = 0; i < iteraciones; i++) {
            TreeMultiset<Cromosoma> survivors = fight(population);
            TreeMultiset<Cromosoma> mutants = mutate(survivors, paquete);
            population = TreeMultiset.create(byCost);
            population.addAll(survivors);
            population.addAll(mutants);
            break;
        }

        Cromosoma winner = population.firstEntry().getElement();

        return winner.getGenes().stream().map(Gen::getVueloAgendado).collect(Collectors.toList());
    }

    private List<VueloAgendado> buildRandomPath(Oficina ofiOrigen, Oficina ofiDestino, LocalDateTime current, Integer depth) {
        List<VueloAgendado> posibles = gestorAlgoritmo.obtenerValidos(ofiOrigen, current);
        if (!posibles.isEmpty()) {
            Collections.shuffle(posibles);
            for (VueloAgendado va : posibles) {
                List<VueloAgendado> vas = new ArrayList<>();
                vas.add(va);
                if (va.getOficinaDestino() == ofiDestino) {
                    return vas;
                } else if (depth < MAX_DEPTH) {
                    List<VueloAgendado> vuelosRecursivos = buildRandomPath(va.getOficinaDestino(), ofiDestino, va.getFechaFin(), depth + 1);
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

        Collection<Cromosoma> syncList = Collections.synchronizedList(new ArrayList<>());


        List<Thread> threads = new ArrayList<>();


        List<Integer> mutables = new ArrayList<>();
        for (int i = 0; i < nmutants; i++) {
            mutables.add(i);
        }

        Collections.shuffle(mutables);

        mutables = mutables.subList(0, nmutants);

        for (Integer mutable : mutables) {
            Thread thread = new Thread(() -> {

                Cromosoma mutant = vas.get(mutable).copy();
                List<Gen> genes = mutant.getGenes();

                Integer index2 = ThreadLocalRandom.current().nextInt(0, genes.size());

                Integer index1;

                if (index2 == 0) {
                    index1 = 0;
                } else {
                    index1 = ThreadLocalRandom.current().nextInt(0, index2);
                }

                VueloAgendado inicio = genes.get(index1).getVueloAgendado();
                VueloAgendado fin = genes.get(index2).getVueloAgendado();

                List<VueloAgendado> reemplazo = new ArrayList<>();

                if (index2 == genes.size() - 1) {
                    if (index1 == 0) {
                        reemplazo = buildRandomPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), paquete.getFechaIngreso(), 0);
                    } else {
                        Integer indicePrevio = index1 - 1;
                        VueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                        reemplazo = buildRandomPath(vueloPrevio.getOficinaDestino(), fin.getOficinaDestino(), vueloPrevio.getFechaFin(), 0);
                    }
                } else {
                    if (index1 == 0) {
                        Integer indiceSgte = index2 + 1;
                        VueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();
                        reemplazo = buildRandomLimitedPath(inicio.getOficinaOrigen(), vueloSgte.getOficinaOrigen(), paquete.getFechaIngreso(), vueloSgte.getFechaInicio());
                    } else {
                        Integer indicePrevio = index1 - 1;
                        Integer indiceSgte = index2 + 1;
                        VueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                        VueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();

                        reemplazo = buildRandomLimitedPath(vueloPrevio.getOficinaDestino(), vueloSgte.getOficinaOrigen(), vueloPrevio.getFechaFin(), vueloSgte.getFechaInicio());
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

                    syncList.add(mutant);
                }

            });

            threads.add(thread);
            thread.run();


        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        for (Cromosoma cromosoma : syncList) {
            population.add(cromosoma);
        }

        return population;
    }

    private void fitness(Cromosoma cromosome, Paquete paquete) {

        double costo = 0;

        for (Gen gen : cromosome.getGenes()) {
            VueloAgendado va = gen.getVueloAgendado();
            Integer capacidadFutura = va.getOficinaDestino().getCapacidadActual() + gestorAlgoritmo.obtenerCapacidadEnMomento(va.getOficinaDestino(), va.getFechaFin());
            double almacenPorcentaje = capacidadFutura / (double) va.getOficinaDestino().getCapacidadMaxima();
            almacenPorcentaje += 2.0;

            costo += va.getPorcentajeUsado() * K1 + almacenPorcentaje * almacenPorcentaje * K2;
        }

        cromosome.setCosto(costo);
    }

}
