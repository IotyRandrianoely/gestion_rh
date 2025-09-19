package com.example.gestion_rh.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.gestion_rh.service.FileStorageService;
import com.example.gestion_rh.service.FileStorageService.FileType;

@Controller
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload-cv")
    @ResponseBody
    public ResponseEntity<?> uploadCV(@RequestParam("file") MultipartFile file) {
        try {
            // Vérifier si le fichier est vide
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner un fichier");
            }

            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (!fileStorageService.isValidFileType(contentType, FileType.DOCUMENT)) {
                return ResponseEntity.badRequest().body("Type de fichier non autorisé. Formats acceptés : "
                        + fileStorageService.getAllowedDocumentTypes());
            }

            String fileName = fileStorageService.storeFile(file, FileType.DOCUMENT);

            return ResponseEntity.ok().body(new FileUploadResponse(fileName, "Document uploadé avec succès"));

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Erreur lors de l'upload : " + ex.getMessage());
        }
    }

    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Vérifier si le fichier est vide
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner une image");
            }

            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (!fileStorageService.isValidFileType(contentType, FileType.IMAGE)) {
                return ResponseEntity.badRequest().body(
                        "Type d'image non autorisé. Formats acceptés : " + fileStorageService.getAllowedImageTypes());
            }

            String fileName = fileStorageService.storeFile(file, FileType.IMAGE);

            return ResponseEntity.ok().body(new FileUploadResponse(fileName, "Image uploadée avec succès"));

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Erreur lors de l'upload : " + ex.getMessage());
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        return downloadFile(fileName, "document");
    }

    @GetMapping("/download/{type}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable String type) {
        try {
            FileType fileType = type.equals("image") ? FileType.IMAGE : FileType.DOCUMENT;
            Path filePath = fileStorageService.getFilePath(fileName, fileType);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Pour les images, afficher directement dans le navigateur
                if (fileType == FileType.IMAGE) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                            .body(resource);
                } else {
                    // Pour les documents, proposer le téléchargement
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .body(resource);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Classe pour la réponse JSON
    public static class FileUploadResponse {
        private String fileName;
        private String message;

        public FileUploadResponse(String fileName, String message) {
            this.fileName = fileName;
            this.message = message;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}