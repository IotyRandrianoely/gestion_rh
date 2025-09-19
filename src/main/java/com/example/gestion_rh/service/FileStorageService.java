package com.example.gestion_rh.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads/cv}")
    private String uploadDir;

    @Value("${app.upload.images.dir:uploads/images}")
    private String imagesDir;

    @Value("${app.upload.allowed-documents:pdf,doc,docx,txt,rtf}")
    private String allowedDocuments;

    @Value("${app.upload.allowed-images:jpg,jpeg,png,gif,bmp,webp}")
    private String allowedImages;

    @Value("${app.upload.max-document-size:10MB}")
    private String maxDocumentSize;

    @Value("${app.upload.max-image-size:5MB}")
    private String maxImageSize;

    public enum FileType {
        DOCUMENT, IMAGE
    }

    public String storeFile(MultipartFile file, FileType fileType) {
        try {
            String targetDir = (fileType == FileType.IMAGE) ? imagesDir : uploadDir;

            // Créer le répertoire s'il n'existe pas
            Path uploadPath = Paths.get(targetDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Nettoyer le nom du fichier
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Vérifier le nom du fichier
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide : " + originalFileName);
            }

            // Vérifier le type et la taille du fichier
            validateFile(file, fileType);

            // Générer un nom unique pour éviter les conflits
            String fileExtension = "";
            if (originalFileName.lastIndexOf(".") > 0) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copier le fichier vers le répertoire de destination
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier. Erreur : " + ex.getMessage(), ex);
        }
    }

    // Méthode de compatibilité pour les CV (documents)
    public String storeFile(MultipartFile file) {
        return storeFile(file, FileType.DOCUMENT);
    }

    private void validateFile(MultipartFile file, FileType fileType) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("Nom de fichier vide");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (fileType == FileType.DOCUMENT) {
            List<String> allowedDocExts = Arrays.asList(allowedDocuments.split(","));
            if (!allowedDocExts.contains(fileExtension)) {
                throw new RuntimeException(
                        "Type de document non autorisé. Formats acceptés : " + allowedDocuments.toUpperCase());
            }

            // Vérifier la taille (10MB pour les documents)
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new RuntimeException("Document trop volumineux. Taille maximum : " + maxDocumentSize);
            }
        } else if (fileType == FileType.IMAGE) {
            List<String> allowedImgExts = Arrays.asList(allowedImages.split(","));
            if (!allowedImgExts.contains(fileExtension)) {
                throw new RuntimeException(
                        "Type d'image non autorisé. Formats acceptés : " + allowedImages.toUpperCase());
            }

            // Vérifier la taille (5MB pour les images)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("Image trop volumineuse. Taille maximum : " + maxImageSize);
            }
        }
    }

    public boolean isValidFileType(String contentType, FileType fileType) {
        if (contentType == null)
            return false;

        if (fileType == FileType.DOCUMENT) {
            return contentType.equals("application/pdf") ||
                    contentType.equals("application/msword") ||
                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                    contentType.equals("text/plain") ||
                    contentType.equals("application/rtf");
        } else if (fileType == FileType.IMAGE) {
            return contentType.startsWith("image/");
        }

        return false;
    }

    public void deleteFile(String fileName, FileType fileType) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                String targetDir = (fileType == FileType.IMAGE) ? imagesDir : uploadDir;
                Path filePath = Paths.get(targetDir).resolve(fileName);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            System.err.println("Erreur lors de la suppression du fichier : " + fileName + " - " + ex.getMessage());
        }
    }

    // Méthode de compatibilité pour les CV (documents)
    public void deleteFile(String fileName) {
        deleteFile(fileName, FileType.DOCUMENT);
    }

    public Path getFilePath(String fileName, FileType fileType) {
        String targetDir = (fileType == FileType.IMAGE) ? imagesDir : uploadDir;
        return Paths.get(targetDir).resolve(fileName);
    }

    // Méthode de compatibilité pour les CV (documents)
    public Path getFilePath(String fileName) {
        return getFilePath(fileName, FileType.DOCUMENT);
    }

    public boolean fileExists(String fileName, FileType fileType) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        Path filePath = getFilePath(fileName, fileType);
        return Files.exists(filePath);
    }

    // Méthode de compatibilité pour les CV (documents)
    public boolean fileExists(String fileName) {
        return fileExists(fileName, FileType.DOCUMENT);
    }

    public String getAllowedDocumentTypes() {
        return allowedDocuments.toUpperCase();
    }

    public String getAllowedImageTypes() {
        return allowedImages.toUpperCase();
    }
}