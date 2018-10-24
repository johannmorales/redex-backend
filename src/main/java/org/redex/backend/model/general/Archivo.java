package org.redex.backend.model.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.redex.backend.model.auditoria.RegistroAuditable;

@Entity
@Table(name = "archivo")
public class Archivo extends RegistroAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String directorio;
    
    @NotEmpty
    @Column(nullable = false)
    private String mimetype;
    
    @NotEmpty
    @Column(nullable = false)
    private String nombreServidor;

    @NotEmpty
    @Column(nullable = false)
    private String nombreOriginal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectorio() {
        return directorio;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getNombreServidor() {
        return nombreServidor;
    }

    public void setNombreServidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

}
