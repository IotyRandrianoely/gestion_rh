package com.example.gestion_rh.controller;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.FileStorageService;

@Controller
@RequestMapping("/admin/files")
public class AdminFilesController {

    private final FileStorageService fileStorageService;
    private final CandidatService candidatService;

    @Value("${app.upload.dir:uploads/cv}")
    private String uploadDir;

    @Value("${app.upload.images.dir:uploads/images}")
    private String imagesDir;

    public AdminFilesController(FileStorageService fileStorageService, CandidatService candidatService) {
        this.fileStorageService = fileStorageService;
        this.candidatService = candidatService;
    }

    @GetMapping
    public String listFiles(Model model, @RequestParam(defaultValue = "all") String type) {
        try {
            List<FileInfo> files = new ArrayList<>();
            List<Candidat> candidats = candidatService.getAll();

            if ("all".equals(type) || "documents".equals(type)) {
                files.addAll(getFilesInDirectory(uploadDir, "document", candidats));
            }

            if ("all".equals(type) || "images".equals(type)) {
                files.addAll(getFilesInDirectory(imagesDir, "image", candidats));
            }

            model.addAttribute("files", files);
            model.addAttribute("selectedType", type);
            model.addAttribute("totalFiles", files.size());

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des fichiers: " + e.getMessage());
        }

        return "admin/files/list";
    }

    @GetMapping("/candidat/{id}")
    public String candidatFiles(@PathVariable Integer id, Model model) {
        try {
            Candidat candidat = candidatService.getById(id);
            if (candidat == null) {
                model.addAttribute("error", "Candidat non trouvé");
                return "redirect:/admin/files";
            }

            List<FileInfo> files = new ArrayList<>();

            if (candidat.getCv() != null && !candidat.getCv().isEmpty()) {
                // Déterminer le type de fichier
                String fileName = candidat.getCv();
                String fileType = isImageFile(fileName) ? "image" : "document";
                String directory = isImageFile(fileName) ? imagesDir : uploadDir;

                Path filePath = Paths.get(directory).resolve(fileName);
                if (Files.exists(filePath)) {
                    try {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(fileName);
                        fileInfo.setFileType(fileType);
                        fileInfo.setSize(Files.size(filePath));
                        fileInfo.setLastModified(Files.getLastModifiedTime(filePath).toMillis());
                        fileInfo.setCandidatNom(candidat.getNom() + " " + candidat.getPrenom());
                        fileInfo.setCandidatId(candidat.getId());
                        files.add(fileInfo);
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la lecture du fichier: " + fileName);
                    }
                }
            }

            model.addAttribute("candidat", candidat);
            model.addAttribute("files", files);

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }

        return "admin/files/candidat";
    }

    private List<FileInfo> getFilesInDirectory(String directory, String fileType, List<Candidat> candidats) {
        List<FileInfo> files = new ArrayList<>();

        try {
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                return files;
            }

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
                for (Path filePath : directoryStream) {
                    if (Files.isRegularFile(filePath)) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(filePath.getFileName().toString());
                        fileInfo.setFileType(fileType);
                        fileInfo.setSize(Files.size(filePath));
                        fileInfo.setLastModified(Files.getLastModifiedTime(filePath).toMillis());

                        // Trouver le candidat associé
                        String fileName = filePath.getFileName().toString();
                        for (Candidat candidat : candidats) {
                            if (fileName.equals(candidat.getCv())) {
                                fileInfo.setCandidatNom(candidat.getNom() + " " + candidat.getPrenom());
                                fileInfo.setCandidatId(candidat.getId());
                                fileInfo.setAnnonceTitle(
                                        candidat.getAnnonce() != null ? candidat.getAnnonce().getProfil() : "");
                                break;
                            }
                        }

                        files.add(fileInfo);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du répertoire: " + directory);
        }

        return files;
    }

    private boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension.matches("jpg|jpeg|png|gif|bmp|webp");
    }

    // Classe pour les informations de fichier
    public static class FileInfo {
        private String fileName;
        private String fileType;
        private long size;
        private long lastModified;
        private String candidatNom;
        private Integer candidatId;
        private String annonceTitle;

        // Getters et setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public String getCandidatNom() {
            return candidatNom;
        }

        public void setCandidatNom(String candidatNom) {
            this.candidatNom = candidatNom;
        }

        public Integer getCandidatId() {
            return candidatId;
        }

        public void setCandidatId(Integer candidatId) {
            this.candidatId = candidatId;
        }

        public String getAnnonceTitle() {
            return annonceTitle;
        }

        public void setAnnonceTitle(String annonceTitle) {
            this.annonceTitle = annonceTitle;
        }

        public String getFormattedSize() {
            if (size < 1024)
                return size + " B";
            if (size < 1024 * 1024)
                return String.format("%.1f KB", size / 1024.0);
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        }
    }
}
