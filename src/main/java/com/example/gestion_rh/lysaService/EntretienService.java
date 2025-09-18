package com.example.gestion_rh.lysaService;

import com.example.gestion_rh.lysaModel.PlaningEntretien;
import com.example.gestion_rh.lysaRepository.PlaningEntretienRepository;
import com.example.gestion_rh.lysaDto.EntretienDTO;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntretienService {

    private final PlaningEntretienRepository repo;

    public EntretienService(PlaningEntretienRepository repo) {
        this.repo = repo;
    }

    public Map<LocalDate, List<EntretienDTO>> getEntretiensGrouped(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        System.out.println("Recherche entretiens entre: " + startDateTime + " et " + endDateTime); // Debug

        // List<PlaningEntretien> entretiens = repo.findByDateDebutBetween(startDateTime, endDateTime);
        List<PlaningEntretien> entretiens = repo.findEntretiensBetweenDates(startDateTime, endDateTime);
        System.out.println("Nombre d'entretiens trouvés: " + entretiens.size()); // Debug
        entretiens.forEach(e -> System.out.println("Entretien: " + e.getId() + " - " + e.getDateDebut())); // Debug

        // Transformation en DTO et groupement par date
        Map<LocalDate, List<EntretienDTO>> grouped = entretiens.stream()
                .map(e -> {
                    String nom = "—";
                    String prenom = "—";
                    String poste = "—";
                    
                    try {
                        if (e.getCandidat() != null) {
                            nom = e.getCandidat().getNom() != null ? e.getCandidat().getNom() : "—";
                            prenom = e.getCandidat().getPrenom() != null ? e.getCandidat().getPrenom() : "—";
                            
                            if (e.getCandidat().getAnnonce() != null) {
                                poste = e.getCandidat().getAnnonce().getProfil() != null 
                                       ? e.getCandidat().getAnnonce().getProfil() : "—";
                            }
                        }
                    } catch (Exception ex) {
                        // Garder les valeurs par défaut
                    }

                    return new EntretienDTO(
                            e.getId(),
                            nom,
                            prenom,
                            poste,
                            e.getDateDebut(),
                            e.getDateFin()
                    );
                })
                .collect(Collectors.groupingBy(dto -> dto.getDateDebut().toLocalDate()));

        System.out.println("Entretiens groupés par date: " + grouped.keySet()); // Debug
        
        return grouped;
    }

    public Map<LocalDate, String> buildSummaryMap(Map<LocalDate, List<EntretienDTO>> grouped) {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");
        Map<LocalDate, String> summaryMap = new HashMap<>();

        grouped.forEach((date, list) -> {
            list.sort(Comparator.comparing(EntretienDTO::getDateDebut));
            String startStr = timeFmt.format(list.get(0).getDateDebut());
            String endStr = timeFmt.format(list.get(list.size() - 1).getDateDebut());
            summaryMap.put(date, "Entretiens prévus de " + startStr + " à " + endStr);
        });

        return summaryMap;
    }

    public Map<LocalDate, String> buildTooltipMap(Map<LocalDate, List<EntretienDTO>> grouped) {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");
        Map<LocalDate, String> tooltipMap = new HashMap<>();

        grouped.forEach((date, list) -> {
            list.sort(Comparator.comparing(EntretienDTO::getDateDebut));
            String tooltipHtml = list.stream().map(e -> {
                String h1 = timeFmt.format(e.getDateDebut());
                String h2 = e.getDateFin() != null ? timeFmt.format(e.getDateFin()) : "";
                return h1 + (h2.isEmpty() ? "" : " à " + h2)
                        + " — " + e.getNom() + " " + e.getPrenom()
                        + " — " + e.getPoste();
            }).collect(Collectors.joining("<br/>"));
            tooltipMap.put(date, tooltipHtml);
        });

        return tooltipMap;
    }
}