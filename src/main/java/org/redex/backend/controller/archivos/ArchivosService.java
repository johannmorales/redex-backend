package org.redex.backend.controller.archivos;

import org.redex.model.general.Archivo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ArchivosService {

    Archivo find(Long fileId);

    Archivo save(MultipartFile file);

}
