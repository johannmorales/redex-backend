package org.redex.backend.algorithm.evolutivo;

import com.google.common.collect.TreeMultiset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import org.redex.backend.algorithm.AlgoritmoOficina;
import org.redex.backend.algorithm.AlgoritmoPaquete;
import org.redex.backend.algorithm.AlgoritmoVueloAgendado;
import org.redex.backend.algorithm.gestor.GestorAlgoritmo;
import org.redex.backend.algorithm.PathNotFoundException;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public class Evolutivo implements Algoritmo {

    private static final Logger logger = LogManager.getLogger(Evolutivo.class);

    private int iteraciones = 250;
    private int populationSize = 25;
    private double surviveRatio = 0.8;
    private double mutationRatio = 0.25;

    private int K1 = 1;
    private int K2 = 1;

    private static final Comparator<Cromosoma> byCost = Comparator.comparingDouble(Cromosoma::getCosto);
    private static final Supplier<TreeMultiset<Cromosoma>> supplier = () -> TreeMultiset.create(byCost);

    private GestorAlgoritmo gestorAlgoritmo;

    @Override
    public List<AlgoritmoVueloAgendado> run(AlgoritmoPaquete paquete, List<AlgoritmoVueloAgendado> vuelosAgendados, List<AlgoritmoOficina> oficinas) {
        gestorAlgoritmo = new GestorAlgoritmo(vuelosAgendados, oficinas);
        TreeMultiset<Cromosoma> population = initialize(paquete.getOficinaOrigen(), paquete.getOficinaDestino(), paquete.getFechaRegistro(), paquete);
        
        for (int i = 0; i < iteraciones; i++) {
            TreeMultiset<Cromosoma> survivors = fight(population);
            TreeMultiset<Cromosoma> mutants = mutate(survivors, paquete);

            population = TreeMultiset.create(byCost);
            population.addAll(survivors);
            population.addAll(mutants);
        }

        Cromosoma winner = population.firstEntry().getElement();
        return winner.getGenes().stream().map(Gen::getVueloAgendado).collect(Collectors.toList());
    }

    private TreeMultiset<Cromosoma> initialize(AlgoritmoOficina ofiOrigen, AlgoritmoOficina ofiDestino, LocalDateTime current, AlgoritmoPaquete paquete) {
        TreeMultiset<Cromosoma> population = TreeMultiset.create(byCost);
        for (int i = 0; i < populationSize; i++) {
            List<AlgoritmoVueloAgendado> list = buildRandomPath(ofiOrigen, ofiDestino, current);
            if (list == null) {
                throw new PathNotFoundException(paquete);
            }
            LinkedList<Gen> genes = list.stream().map(va -> new Gen(va)).collect(Collectors.toCollection(() -> new LinkedList<>()));

            Cromosoma cromosome = new Cromosoma();
            cromosome.setGenes(genes);
            fitness(cromosome, paquete);
            population.add(cromosome);
        }
        return population;
    }

    private List<AlgoritmoVueloAgendado> buildRandomPath(AlgoritmoOficina ofiOrigen, AlgoritmoOficina ofiDestino, LocalDateTime current) {
        List<AlgoritmoVueloAgendado> posibles = gestorAlgoritmo.obtenerValidos(ofiOrigen, current);
        if (!posibles.isEmpty()) {
            Collections.shuffle(posibles);
            for (AlgoritmoVueloAgendado va : posibles) {
                List<AlgoritmoVueloAgendado> vas = new ArrayList<>();
                vas.add(va);
                if (va.getOficinaDestino() == ofiDestino) {
                    return vas;
                } else {
                    List<AlgoritmoVueloAgendado> vuelosRecursivos = buildRandomPath(va.getOficinaDestino(), ofiDestino, va.getFechaFin());
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

    private List<AlgoritmoVueloAgendado> buildRandomLimitedPath(AlgoritmoOficina ofiOrigen, AlgoritmoOficina ofiDestino, LocalDateTime current, LocalDateTime limit) {
        List<AlgoritmoVueloAgendado> posibles = gestorAlgoritmo.obtenerValidos(ofiOrigen, current)
                .stream()
                .filter(va -> va.getFechaInicio().isBefore(limit) || va.getFechaInicio().isEqual(limit))
                .collect(Collectors.toList());

        if (!posibles.isEmpty()) {
            Collections.shuffle(posibles);

            for (AlgoritmoVueloAgendado va : posibles) {

                List<AlgoritmoVueloAgendado> vas = new ArrayList<>();
                vas.add(va);

                if (va.getOficinaDestino() == ofiDestino) {
                    return vas;
                } else {
                    List<AlgoritmoVueloAgendado> vuelosRecursivos = buildRandomLimitedPath(va.getOficinaDestino(), ofiDestino, va.getFechaFin(), limit);
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

    private TreeMultiset<Cromosoma> mutate(TreeMultiset<Cromosoma> survivors, AlgoritmoPaquete paquete) {
        TreeMultiset<Cromosoma> population = TreeMultiset.create(byCost);
        Integer nmutants = (int) (survivors.size() * mutationRatio);
        ArrayList<Cromosoma> vas = new ArrayList<>(survivors);

        ThreadLocalRandom.current().ints(0, survivors.size()).distinct().limit(nmutants).forEach(index -> {
            Cromosoma mutant = vas.get(index).copy();
            final List<Gen> genes = mutant.getGenes();
            Integer indexToMutate = ThreadLocalRandom.current().nextInt(0, mutant.getGenes().size() + 1);

            Integer index2 = ThreadLocalRandom.current().nextInt(0, mutant.getGenes().size());
            Integer index1 = ThreadLocalRandom.current().nextInt(0, index2 + 1);

            AlgoritmoVueloAgendado inicio = genes.get(index1).getVueloAgendado();
            AlgoritmoVueloAgendado fin = genes.get(index2).getVueloAgendado();

            List<AlgoritmoVueloAgendado> reemplazo = new ArrayList<>();

            if (index2 == genes.size() - 1) {
                if (index1 == 0) {
                    reemplazo = buildRandomPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), paquete.getFechaRegistro());
                } else {
                    Integer indicePrevio = index1 - 1;
                    AlgoritmoVueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                    reemplazo = buildRandomPath(vueloPrevio.getOficinaDestino(), fin.getOficinaDestino(), vueloPrevio.getFechaInicio());
                }
            } else {
                if (index1 == 0) {
                    Integer indiceSgte = index2 + 1;
                    AlgoritmoVueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();
                    reemplazo = buildRandomLimitedPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), paquete.getFechaRegistro(), vueloSgte.getFechaFin());
                } else {
                    Integer indicePrevio = index1 - 1;
                    Integer indiceSgte = index2 + 1;
                    AlgoritmoVueloAgendado vueloPrevio = genes.get(indicePrevio).getVueloAgendado();
                    AlgoritmoVueloAgendado vueloSgte = genes.get(indiceSgte).getVueloAgendado();

                    reemplazo = buildRandomLimitedPath(inicio.getOficinaOrigen(), fin.getOficinaDestino(), vueloPrevio.getFechaInicio(), vueloSgte.getFechaFin());
                }
            }

            LinkedList<Gen> nuevos = new LinkedList<>();

            for (int i = 0; i < index1; i++) {
                nuevos.add(genes.get(i));
            }

            if (reemplazo == null) {

            } else {

                for (AlgoritmoVueloAgendado vueloAgendado : reemplazo) {
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

    private void fitness(Cromosoma cromosome, AlgoritmoPaquete paquete) {

        double costo = 0;

        for (Gen gen : cromosome.getGenes()) {
            AlgoritmoVueloAgendado va = gen.getVueloAgendado();
            costo += gen.getVueloAgendado().getPorcentajeUsado() * K1 + 2 * K2;
        }

        AlgoritmoVueloAgendado lastFlight = cromosome.getGenes().get(cromosome.getGenes().size() - 1).getVueloAgendado();

        if (lastFlight.getOficinaDestino() != paquete.getOficinaDestino()) {
            costo = costo + 1000000;
        }

        LocalDateTime inicio = paquete.getFechaRegistro();
        LocalDateTime fin = cromosome.getGenes().get(cromosome.getGenes().size() - 1).getVueloAgendado().getFechaInicio();

        costo += ChronoUnit.MINUTES.between(inicio, fin);

        cromosome.setCosto(costo);
    }

    @Override
    public List<SimulacionVueloAgendado> runBatch(List<SimulacionPaquete> paquetes, List<VueloAgendado> vuelosAgendados, List<SimulacionOficina> oficinas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
