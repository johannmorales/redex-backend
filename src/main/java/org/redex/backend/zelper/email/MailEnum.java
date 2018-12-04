package org.redex.backend.zelper.email;

public enum MailEnum {

    NUEVO_USUARIO("usuarios/nuevoUsuario", "Cuenta registrada"),
    REESTABLECER_CONTRASENA("restablecerContrasena", "Su contrasena ha sido reestablecida"),

    REGISTRO_REMITENTE("registroRemitente", "Su paquete ha sido registrado"),
    REGISTRO_DESTINATARIO("registroDestinatario", "Su paquete ha sido registrado"),
    
    NOTIFICACION_ENVIO("notificacionVuelo","Su paquete ha sido embarcado en un vuelo"),
    
    NOTIFICACION_LLEGADA_REMITENTE("notificacionLlegadaRemitente","Su paquete fue recogido con exito"),
    
    NOTIFICACION_LLEGADA_DESTINATARIO("notificacionLlegadaDestinatario","Su paquete esta listo para ser recogido");

    private String template;

    private String defaultSubject;

    MailEnum(String template, String defaultSubject) {
        this.template = template;
        this.defaultSubject = defaultSubject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDefaultSubject() {
        return defaultSubject;
    }

    public void setDefaultSubject(String defaultSubject) {
        this.defaultSubject = defaultSubject;
    }


}
