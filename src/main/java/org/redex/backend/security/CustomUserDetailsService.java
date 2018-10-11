package org.redex.backend.security;

import org.redex.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.redex.backend.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuariosRepository;

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
