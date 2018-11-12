package org.redex.backend.algorithm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.algorithm.evolutivo.Evolutivo;
import org.redex.backend.model.envios.Paquete;
import org.redex.backend.model.envios.VueloAgendado;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.model.simulacion.SimulacionOficina;
import org.redex.backend.model.simulacion.SimulacionPaquete;
import org.redex.backend.model.simulacion.SimulacionVueloAgendado;

public class AlgoritmoWrapper {

    public static final Logger logger = LogManager.getLogger(AlgoritmoWrapper.class);

    public static List<SimulacionVueloAgendado> simulacionRunBatch(List<SimulacionPaquete> paquetes, List<SimulacionVueloAgendado> vuelosAgendados, List<SimulacionOficina> oficinas) {
        List<AlgoritmoOficina> ao = oficinas.stream().map(o -> AlgoritmoOficina.of(o)).collect(Collectors.toList());
        Map<String, AlgoritmoOficina> mapOficinas = ao.stream().collect(Collectors.toMap(x -> x.getCodigo(), x -> x));

        List<AlgoritmoPaquete> pqs = paquetes.stream().map(p -> AlgoritmoPaquete.of(p, mapOficinas)).collect(Collectors.toList());

        List<AlgoritmoVueloAgendado> sva = vuelosAgendados.stream().map(va -> AlgoritmoVueloAgendado.of(va, mapOficinas)).collect(Collectors.toList());

        return null;

    }

    public static List<SimulacionVueloAgendado> simulacionRun(SimulacionPaquete paquete, List<SimulacionVueloAgendado> vuelosAgendados, List<SimulacionVueloAgendado> vuelosTerminados, List<SimulacionOficina> oficinas) {
        long t0 = System.nanoTime();

        Map<Long, SimulacionVueloAgendado> vaId = vuelosAgendados.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        List<AlgoritmoOficina> ao = oficinas.stream().map(o -> AlgoritmoOficina.of(o)).collect(Collectors.toList());
        Map<String, AlgoritmoOficina> mapOficinas = ao.stream().collect(Collectors.toMap(x -> x.getCodigo(), x -> x));
        List<AlgoritmoVueloAgendado> sva = vuelosAgendados.stream().map(va -> AlgoritmoVueloAgendado.of(va, mapOficinas)).collect(Collectors.toList());
        AlgoritmoPaquete p = AlgoritmoPaquete.of(paquete, mapOficinas);
        List<AlgoritmoVueloAgendado> svaTerminados = vuelosTerminados.stream().map(va -> AlgoritmoVueloAgendado.of(va, mapOficinas)).collect(Collectors.toList());

        Evolutivo e = new Evolutivo();

        long t1 = System.nanoTime();

        List<AlgoritmoVueloAgendado> avas = e.run(p, sva, svaTerminados, ao);

        long t2 = System.nanoTime();

        logger.info("\t\t\t\t[wrapper]               ({} ms)", (t1-t0)/1000000);
        logger.info("\t\t\t\t[algoritmo]             ({} ms)", (t2-t1)/1000000);
        return avas.stream().map(x -> vaId.get(x.getId())).collect(Collectors.toList());
    }

    public static List<VueloAgendado> sistemaRun(Paquete paquete, List<VueloAgendado> vuelosAgendados, List<VueloAgendado> vuelosTerminados, List<Oficina> oficinas) {
        Map<Long, VueloAgendado> vaId = vuelosAgendados.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        List<AlgoritmoOficina> ao = oficinas.stream().map(o -> AlgoritmoOficina.of(o)).collect(Collectors.toList());
        Map<String, AlgoritmoOficina> mapOficinas = ao.stream().collect(Collectors.toMap(x -> x.getCodigo(), x -> x));
        List<AlgoritmoVueloAgendado> sva = vuelosAgendados.stream().map(va -> AlgoritmoVueloAgendado.of(va, mapOficinas)).collect(Collectors.toList());
        AlgoritmoPaquete p = AlgoritmoPaquete.of(paquete, mapOficinas);
        List<AlgoritmoVueloAgendado> svaTerminados = vuelosTerminados.stream().map(va -> AlgoritmoVueloAgendado.of(va, mapOficinas)).collect(Collectors.toList());

        Evolutivo e = new Evolutivo();
        List<AlgoritmoVueloAgendado> avas = e.run(p, sva, svaTerminados, ao);

        return avas.stream().map(x -> vaId.get(x.getId())).collect(Collectors.toList());
    }
}
