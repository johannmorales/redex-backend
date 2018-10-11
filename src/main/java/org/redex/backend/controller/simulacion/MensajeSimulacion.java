package org.redex.backend.controller.simulacion;

public class MensajeSimulacion {

    private enum Estado {
        DESCONOCIDO, EJECUTANDO, FINALIZADO
    }

    private Estado estado;

    private Integer porcentaje;

    private Integer actual;

    private Integer maximo;

    public static MensajeSimulacion crearFinalizado() {
        MensajeSimulacion ms = new MensajeSimulacion();
        ms.setActual((Integer) 0);
        ms.setMaximo((Integer) 0);
        ms.setPorcentaje((Integer) 0);
        ms.setEstado(Estado.FINALIZADO);

        return ms;
    }

    public static MensajeSimulacion crearEjecutando(Integer actual, Integer maximo) {
        MensajeSimulacion ms = new MensajeSimulacion();
        ms.setActual(actual);
        ms.setMaximo(maximo);
        ms.setPorcentaje((Integer) actual * 100 / maximo);
        ms.setEstado(Estado.EJECUTANDO);

        return ms;
    }

    public static MensajeSimulacion crearDesconocido() {
        MensajeSimulacion ms = new MensajeSimulacion();
        ms.setActual((Integer) 0);
        ms.setMaximo((Integer) 0);
        ms.setPorcentaje((Integer) 0);
        ms.setEstado(Estado.DESCONOCIDO);

        return ms;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

}
