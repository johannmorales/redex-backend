package org.redex.backend.algorithm.evolutivo;

import org.redex.backend.model.envios.VueloAgendado;

public class Gen {

    public Gen(VueloAgendado va) {
        this.vueloAgendado = va;
    }

    private VueloAgendado vueloAgendado;

    public VueloAgendado getVueloAgendado() {
        return vueloAgendado;
    }

    public void setVueloAgendado(VueloAgendado vueloAgendado) {
        this.vueloAgendado = vueloAgendado;
    }

}
