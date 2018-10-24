package org.redex.backend.model.rrhh;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.redex.backend.model.general.EstadoEnum;
import org.redex.backend.model.general.Persona;

@Entity
@Table(name = "colaborador")
public class Colaborador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;
    
    @ManyToOne
    @JoinColumn(name = "id_oficina", nullable = false)
    private Oficina oficina;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoEnum cargo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;
    
    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Column
    private String telefono;

    @Column
    private String celular;

    public Colaborador() {
    }

    public Colaborador(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public CargoEnum getCargo() {
        return cargo;
    }

    public void setCargo(CargoEnum cargo) {
        this.cargo = cargo;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

}
