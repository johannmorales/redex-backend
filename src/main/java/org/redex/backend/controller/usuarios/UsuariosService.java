package org.redex.backend.controller.usuarios;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.seguridad.Usuario;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UsuariosService {

    public CargaDatosResponse carga(MultipartFile file);

    public List<Usuario> all();

    public void crearUsuario(UsuariosPayload usuario);

    public void restablecerContraseña(UsuariosPayload usuario);

    public Page<Usuario> crimsonList(CrimsonTableRequest request);

    public void desactivar(Long id);

    public void activar(Long id);

    public void editar(Usuario usuario);

    public Usuario find(Long id);
}
