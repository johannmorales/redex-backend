package org.redex.backend.controller.oficinas;

import org.redex.backend.model.rrhh.Oficina;

public class OficinaWrapper {

    public String id;

    public String pais;

    public String capacidad;

    public String codigo;

    public static OficinaWrapper of(Oficina oficina) {
        OficinaWrapper wrapper = new OficinaWrapper();

        wrapper.id = oficina.getId().toString();
        wrapper.capacidad = oficina.getCapacidadMaxima().toString();
        wrapper.pais = oficina.getPais().getId().toString();
        wrapper.codigo = oficina.getCodigo();

        return wrapper;
    }

    public Oficina build() {
        Oficina oficina = new Oficina();
        if (id != null && !id.isEmpty()) {
            oficina.setId(Long.parseLong(id));
        }
        return oficina;
    }

}
