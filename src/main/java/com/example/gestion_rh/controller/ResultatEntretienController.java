package com.example.gestion_rh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestion_rh.model.ResultatEntretien;
import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.ResultatEntretienService;

@RestController
@RequestMapping("/candidat")
// candidats/noter_entretien
public class ResultatEntretienController {

    @Autowired
    private ResultatEntretienService service;
    @Autowired
    private ResultatEntretienService resultatEntretienService;
    @Autowired
    private  CandidatService candidatService;

    // @PostMapping("/noter_entretien")
    // public String noterEntretien(@RequestParam("candidatId") Long candidatId,
    //                              @RequestParam("annonceId") Long annonceId,
    //                              @RequestParam("niveau") Integer niveau) {
    //     if (niveau == null) {
    //         return "redirect:/candidats?error=missing_niveau";
    //     }

    //     ResultatEntretien re = new ResultatEntretien();
    //     // adapter les setters si vos champs utilisent Integer/Long ou int
    //     re.setIdCandidat(candidatId != null ? candidatId.intValue() : 0);
    //     // re.setIdAnnonce(annonceId != null ? annonceId.intValue() : 0);
    //     re.setNiveau(niveau);

    //     resultatEntretienService.save(re);
    //     return "redirect:/candidats/list";
   

    @GetMapping
     List<ResultatEntretien> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultatEntretien> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResultatEntretien create(@RequestBody ResultatEntretien resultatEntretien) {
        return service.save(resultatEntretien);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}