package org.redex.backend.controller.usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.redex.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional(readOnly = true)
public class UsuariosServiceImp implements UsuariosService{
    
    @Autowired
    UsuariosRepository usuariosRepository;
    
    @Transactional
    public void crearUsuario(UsuariosPayload usuario){
    
    }
    @Transactional
    public void activar(UsuariosPayload usuario){
    }
    @Transactional
    public void desactivar(UsuariosPayload usuario){
        
    }
    @Transactional
    public void restablecerContraseña(UsuariosPayload usuario){
        
    }

}
