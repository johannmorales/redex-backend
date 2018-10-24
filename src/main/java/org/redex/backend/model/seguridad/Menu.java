package org.redex.backend.model.seguridad;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_menu_superior")
    private Menu menuSuperior;

    @Column
    @NotBlank
    private String nombre;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuTipoEnum tipo;

    @Column
    private String ruta;

    @Column
    private String icono;

    @Column
    private Integer orden;

    @Column
    private Integer nivel;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu getMenuSuperior() {
        return menuSuperior;
    }

    public void setMenuSuperior(Menu menuSuperior) {
        this.menuSuperior = menuSuperior;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MenuTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(MenuTipoEnum tipo) {
        this.tipo = tipo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

}
