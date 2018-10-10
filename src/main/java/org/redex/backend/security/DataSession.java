package org.redex.backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.redex.model.Colaborador;
import org.redex.model.Oficina;
import org.redex.model.Persona;
import org.redex.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DataSession implements UserDetails {

    private Long id;

    private String username;

    private Persona persona;
    
    private Oficina oficina;
    
    private Colaborador colaborador;
    
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public DataSession(Long id, String username, String password, Colaborador colaborador, Oficina oficina, Persona persona, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.colaborador = colaborador;
        this.oficina = oficina;
        this.persona = persona;
    }

    public static DataSession create(Usuario user) {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRol().getCodigo().name()));

        return new DataSession(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getColaborador(),
                user.getColaborador().getOficina(),
                user.getColaborador().getPersona(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSession that = (DataSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
