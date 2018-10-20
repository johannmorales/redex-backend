package org.redex.backend.controller.archivos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redex.backend.zelper.exception.MyFileNotFoundException;
import org.redex.model.general.Archivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("archivos")
public class ArchivosController {

    private static final Logger logger = LogManager.getLogger(ArchivosController.class);

    @Autowired
    private ArchivosService service;

    @PostMapping("/upload")
    public UploadFileResponse upload(@RequestParam("file") MultipartFile file) {
        Archivo archivo = service.save(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/archivos/download/")
                .path("" + archivo.getId())
                .toUriString();

        return new UploadFileResponse(archivo.getId(), fileDownloadUri,
                file.getContentType(), archivo.getNombreOriginal(), archivo.getNombreServidor());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId, HttpServletRequest request) {
        Archivo archivo = service.find(fileId);

        try {
            
           Path path = Paths.get(archivo.getDirectorio())
                .toAbsolutePath().normalize();

           Path filePath = path.resolve(archivo.getNombreServidor()).normalize();
           
           Resource resource = new UrlResource(filePath.toUri());
           
            if (!resource.exists()) {
                throw new MyFileNotFoundException("Archivo no encontrado " + archivo.getNombreOriginal());
            }
            
            String contentType = null;
            
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                logger.info("No se puedo determinar el tipo del archivo");
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombreOriginal()+ "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Archivo no encontrado " + fileId, ex);
        }
    }

}
