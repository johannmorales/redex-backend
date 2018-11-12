package org.redex.backend.controller.personas;

import org.redex.backend.model.general.Persona;
import org.redex.backend.model.general.TipoDocumentoIdentidad;

public class PersonaRegistro {

    private String docIdentidad;

    private String nombres;

    private String telefono;

    private Long idDocumento;

    private String apMaterno;

    private String apPaterno;

    private String correoElectronico;

    public Persona crearPersona () {
        Persona p = new Persona();
        p.setNumeroDocumentoIdentidad(this.docIdentidad);
        p.setEmail(correoElectronico);
        p.setPaterno(apPaterno);
        p.setNombres(nombres);
        p.setTelefono(telefono);
        p.setMaterno(apMaterno);
        p.setTipoDocumentoIdentidad(new TipoDocumentoIdentidad(idDocumento));
        return p;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getDocIdentidad() {
        return docIdentidad;
    }

    public void setDocIdentidad(String docIdentidad) {
        this.docIdentidad = docIdentidad;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

}
