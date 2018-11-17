package org.redex.backend.controller.usuarios;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.seguridad.Usuario;
import org.redex.backend.zelper.crimsontable.CrimsonTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UsuariosService {

    CargaDatosResponse carga(MultipartFile file);

    List<Usuario> all();

    void crearUsuario(UsuariosPayload usuario);

    void restablecerContraseña(UsuariosPayload usuario);

    Page<Usuario> crimsonList(CrimsonTableRequest request);

    void desactivar(Long id);

    void activar(Long id);

    void editar(Usuario usuario);

    Usuario find(Long id);
}
