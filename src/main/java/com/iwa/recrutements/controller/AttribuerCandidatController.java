package com.iwa.recrutements.controller;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.service.AttribuerCandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributions")
public class AttribuerCandidatController {

    private AttribuerCandidatService attribuerCandidatService;

    @Autowired
    public AttribuerCandidatController(AttribuerCandidatService attribuerCandidatService) {
        this.attribuerCandidatService = attribuerCandidatService;
    }

    // Get all attributions
    @GetMapping
    public ResponseEntity<List<AttribuerCandidat>> getAllAttributions() {
        return ResponseEntity.ok(attribuerCandidatService.getAllAttributions());
    }

    // Get attribution by id offre
    @GetMapping("/offre/{idOffre}")
    public ResponseEntity<List<AttribuerCandidat>> getAttributionsByIdOffre(@PathVariable Long idOffre) {
        return ResponseEntity.ok(attribuerCandidatService.getAttributionsByIdOffre(idOffre));
    }

    // Get attribution by email candidat
    @GetMapping("/candidat/{emailCandidat}")
    public ResponseEntity<List<AttribuerCandidat>> getAttributionsByEmailCandidat(@PathVariable String emailCandidat) {
        return ResponseEntity.ok(attribuerCandidatService.getAttributionsByEmailCandidat(emailCandidat));
    }

    @PostMapping
    public ResponseEntity<AttribuerCandidat> createAttribution(@RequestBody AttribuerCandidat attribuerCandidat) {
        return ResponseEntity.ok(attribuerCandidatService.saveAttribution(attribuerCandidat));
    }

    @PutMapping
    public ResponseEntity<AttribuerCandidat> updateAttribution(@RequestBody AttribuerCandidat attribuerCandidat) {
        return ResponseEntity.ok(attribuerCandidatService.saveAttribution(attribuerCandidat));
    }

    // delete attribution by id offre and email candidat
    @DeleteMapping("/{idOffre}/{emailCandidat}")
    public ResponseEntity<Void> deleteAttribution(@PathVariable Long idOffre, @PathVariable String emailCandidat) {
        attribuerCandidatService.deleteAttribution(idOffre, emailCandidat);
        return ResponseEntity.noContent().build();
    }

    // Autres endpoints
}
