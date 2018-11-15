package org.redex.backend.zelper.email;

public enum MailEnum {

    NUEVO_USUARIO("usuarios/nuevoUsuario", "Cuenta registrada"),
    REESTABLECER_CONTRASENA("usuarios/restablecerContrasena", "Su contrasena ha sido reestablecida");

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
