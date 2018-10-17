        package org.redex.backend.security;

import org.redex.backend.repository.UsuariosRepository;
import org.redex.model.seguridad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UsuariosRepository usuariosRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Usuario %s no encontrado", username)));

        return DataSession.create(usuario);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Usuario user = usuariosRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Usuario %d no encontrado", id)));

        return DataSession.create(user);
    }
}
