package org.redex.backend.controller.usuarios;

import org.redex.backend.model.general.TipoDocumentoIdentidad;
import org.redex.backend.model.rrhh.Oficina;
import org.redex.backend.model.seguridad.Rol;


public class UsuariosPayload {

    private String nombres;

    private String aPaterno;

    private String aMaterno;

    private TipoDocumentoIdentidad tipDoc;

    private String docId;

    private String email;

    private String telefono;

    private Rol rol;

    private Oficina oficina;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public TipoDocumentoIdentidad getTipDoc() {
        return tipDoc;
    }

    public void setTipDoc(TipoDocumentoIdentidad tipDoc) {
        this.tipDoc = tipDoc;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }
}
