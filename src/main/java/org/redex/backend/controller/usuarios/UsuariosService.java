package org.redex.backend.controller.usuarios;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.model.general.Archivo;
import org.redex.model.seguridad.Usuario;

public interface UsuariosService {
    public CargaDatosResponse carga(Archivo archivo);

    public List<Usuario> all();
}
