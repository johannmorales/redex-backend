package org.redex.backend.model.auditoria;

public enum AuditoriaTipoEnum {
    REGISTRO_PAQUETES("Registro de paquete"),

    REPORTE_PAQUETES_POR_VUELO("Emision de reporte de paquetes por vuelo"),
    REPORTE_PAQUETES_POR_USUARIO("Emision de reporte de paquetes registrados por usuario"),
    REPORTE_ENVIOS_POR_FECHAS("Emision de reporte de envios por fechas"),
    REPORTE_ENVIOS_POR_OFICINAS("Emision de reporte de envios por oficinas"),
    REPORTE_ENVIOS_FINALIZADOS("Emision de reporte de envios finalizados"),
    REPORTE_AUDITORIA("Emision de reporte de auditoria"),

    REGISTRO_SIMULACION("Registro de simulacion");

    private String descripcion;

    AuditoriaTipoEnum(String descripcion){
        this.setDescripcion(descripcion);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
