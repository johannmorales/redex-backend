package org.redex.backend.zelper.pdf;

public enum PdfEnum {

    PLAN_VUELO("planVuelo");

    private String template;

    PdfEnum(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
