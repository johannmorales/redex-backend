package org.redex.backend.controller.archivos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.redex.backend.config.FileConfig;
import org.redex.backend.repository.ArchivosRepository;
import org.redex.backend.zelper.exception.FileStorageException;
import org.redex.backend.zelper.exception.MyFileNotFoundException;
import org.redex.model.general.Archivo;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArchivosServiceImp implements ArchivosService {
    
    private ArchivosRepository archivosRepository;
    
    private final Path fileStorageLocation;

    @Autowired
    public ArchivosServiceImp(FileConfig fileConfig, ArchivosRepository archivosRepository) {
        this.fileStorageLocation = Paths.get(fileConfig.getUploadDir())
                .toAbsolutePath().normalize();
        
        this.archivosRepository = archivosRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("No se puedo crear el directorio en el que se guardaran los archivos subidos.", ex);
        }
    }

    @Transactional
    public Archivo save(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String serverName = "" + System.currentTimeMillis();
            
            Path targetLocation = this.fileStorageLocation.resolve(serverName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            Archivo archivo = new Archivo();
            
            archivo.setMimetype(file.getContentType());
            archivo.setDirectorio(this.fileStorageLocation.toString());
            archivo.setNombreOriginal(fileName);
            archivo.setNombreServidor(serverName);
            
            archivosRepository.save(archivo);
            
            return archivo;
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo guardar el archivo " + fileName + ".Por favor intente de nuevo!", ex);
        }
    }

    public Archivo find(Long fileId) {
            Archivo archivo = archivosRepository.findById(fileId)
                    .orElseThrow(() -> new MyFileNotFoundException("Archivo no encontrado " + fileId));
            
            return archivo;
    }
    
}
