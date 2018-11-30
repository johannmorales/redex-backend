package org.redex.backend.controller.paquetes;

import java.util.List;


public class TrackReport {

    private int status;

    private String codigoRastreo;

    private String origen;

    private String destino;

    private String estado;

    private String localizacion;

    private String personaDestino;

    private List<PackageRoute> plan;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public List<PackageRoute> getPlan() {
        return plan;
    }

    public void setPlan(List<PackageRoute> plan) {
        this.plan = plan;
    }

    public String getCodigoRastreo() {
        return codigoRastreo;
    }

    public void setCodigoRastreo(String codigoRastreo) {
        this.codigoRastreo = codigoRastreo;
    }

    public String getPersonaDestino() {
        return personaDestino;
    }

    public void setPersonaDestino(String personaDestino) {
        this.personaDestino = personaDestino;
    }
}
