package org.redex.backend.zelper.response;

import java.util.List;

public class CargaDatosResponse {

    private Integer cantidadErrores;

    private Integer cantidadRegistros;

    private String mensaje;

    private List<String> errores;

    public CargaDatosResponse(Integer cantidadErrores, Integer cantidadRegistros, String mensaje, List<String> errores) {
        this.cantidadErrores = cantidadErrores;
        this.cantidadRegistros = cantidadRegistros;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public Integer getCantidadErrores() {
        return cantidadErrores;
    }

    public void setCantidadErrores(Integer cantidadErrores) {
        this.cantidadErrores = cantidadErrores;
    }

    public Integer getCantidadRegistros() {
        return cantidadRegistros;
    }

    public void setCantidadRegistros(Integer cantidadRegistros) {
        this.cantidadRegistros = cantidadRegistros;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

}
