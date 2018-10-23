package org.redex.backend.controller.usuario;

public class UsuariosService {
    public void crearUsuario(UsuariosPayload usuario);
    public void activar(UsuariosPayload usuario);
    public void desactivar(UsuariosPayload usuario);
    public void restablecerContraseña(UsuariosPayload usuario);
}
