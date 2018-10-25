package org.redex.backend.controller.usuarios;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.seguridad.Usuario;
import org.springframework.web.multipart.MultipartFile;

public interface UsuariosService {

    public CargaDatosResponse carga(MultipartFile file);

    public List<Usuario> all();

    public void crearUsuario(UsuariosPayload usuario);

    public void activar(UsuariosPayload usuario);

    public void desactivar(UsuariosPayload usuario);

    public void restablecerContraseña(UsuariosPayload usuario);

}
