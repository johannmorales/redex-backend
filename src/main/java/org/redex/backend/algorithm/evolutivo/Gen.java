package org.redex.backend.algorithm.evolutivo;

import org.redex.backend.algorithm.AlgoritmoVueloAgendado;

public class Gen {

    public Gen(AlgoritmoVueloAgendado va) {
        this.vueloAgendado = va;
    }

    private AlgoritmoVueloAgendado vueloAgendado;

    public AlgoritmoVueloAgendado getVueloAgendado() {
        return vueloAgendado;
    }

    public void setVueloAgendado(AlgoritmoVueloAgendado vueloAgendado) {
        this.vueloAgendado = vueloAgendado;
    }

}
