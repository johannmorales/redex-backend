package org.redex.backend.controller.oficinas;

import java.util.List;
import org.redex.backend.zelper.response.CargaDatosResponse;
import org.redex.backend.model.rrhh.Colaborador;
import org.redex.backend.model.rrhh.Oficina;
import org.springframework.web.multipart.MultipartFile;

public interface OficinasService {

    public void cambiarJefe(Oficina oficina, Colaborador colaborador);

    public void agregarColaborador(Colaborador colaborador);

    public CargaDatosResponse carga(MultipartFile file);

    public List<Oficina> all();

    public void desactivar(Long id);

    public void activar(Long id);

    public Oficina find(Long id);

    public void save(Oficina oficina);

    public void update(Oficina oficina);
    
}
