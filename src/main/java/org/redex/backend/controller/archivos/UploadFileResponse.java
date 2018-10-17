 package org.redex.backend.controller.archivos;

public class UploadFileResponse {

    private Long id;
    private String downloadUri;
    private String fileType;
    private String nombreOriginal;
    private String nombreServidor;

    public UploadFileResponse(Long id, String downloadUri, String fileType, String nombreOriginal, String nombreServidor) {
        this.id = id;
        this.downloadUri = downloadUri;
        this.fileType = fileType;
        this.nombreOriginal = nombreOriginal;
        this.nombreServidor = nombreServidor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getNombreServidor() {
        return nombreServidor;
    }

    public void setNombreServidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

}
