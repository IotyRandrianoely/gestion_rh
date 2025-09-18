package com.example.gestion_rh.lysaController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestion_rh.lysaDto.EntretienDTO;
import com.example.gestion_rh.lysaService.EntretienService;

@RestController
@RequestMapping("/api/entretiens")
public class EntretienController {

    @Autowired
    private EntretienService entretienService;

    @GetMapping("/{year}/{month}")
    public Map<String, Object> getEntretiens(@PathVariable int year, @PathVariable int month) {
        YearMonth ym = YearMonth.of(year, month);
        
        // Pour être sûr de récupérer tous les entretiens du mois affiché par FullCalendar
        // On prend une période plus large : du début du mois précédent à la fin du mois suivant
        LocalDate startRange = ym.minusMonths(1).atDay(1);
        LocalDate endRange = ym.plusMonths(1).atEndOfMonth();

        System.out.println("Période de recherche: " + startRange + " à " + endRange); // Debug

        // Récupérer les entretiens groupés par date
        Map<LocalDate, List<EntretienDTO>> entretiensGrouped = 
            entretienService.getEntretiensGrouped(startRange, endRange);

        // Transformer les données pour FullCalendar
        Map<String, Object> response = new java.util.HashMap<>();
        
        // Convertir LocalDate en String pour JSON
        Map<String, List<EntretienDTO>> entretiensForJson = new java.util.HashMap<>();
        entretiensGrouped.forEach((date, entretiens) -> {
            entretiensForJson.put(date.toString(), entretiens);
        });
        
        response.put("entretiens", entretiensForJson);
        
        return response;
    }
}