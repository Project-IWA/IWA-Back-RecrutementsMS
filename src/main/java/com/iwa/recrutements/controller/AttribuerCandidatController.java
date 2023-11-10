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

    @Autowired
    private AttribuerCandidatService attribuerCandidatService;

    @GetMapping
    public ResponseEntity<List<AttribuerCandidat>> getAllAttributions() {
        return ResponseEntity.ok(attribuerCandidatService.getAllAttributions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttribuerCandidat> getAttributionById(@PathVariable Long id) {
        return attribuerCandidatService.getAttributionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AttribuerCandidat> createOrUpdateAttribution(@RequestBody AttribuerCandidat attribuerCandidat) {
        return ResponseEntity.ok(attribuerCandidatService.saveOrUpdateAttribution(attribuerCandidat));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribution(@PathVariable Long id) {
        attribuerCandidatService.deleteAttribution(id);
        return ResponseEntity.noContent().build();
    }

    // Autres endpoints
}
