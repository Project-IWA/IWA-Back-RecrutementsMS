package com.iwa.recrutements.controller;

import com.iwa.recrutements.model.Candidat;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreController {

    @Autowired
    private OffreService offreService;

    // Get all offres
    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres() {
        return ResponseEntity.ok(offreService.getAllOffres());
    }

    // Get offre by id
    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.getOffreById(id));
    }

    // Endpoint pour récupérer les offres avec attributions et infos candidats pour un utilisateur donné
    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Offre>> getOffresWithAttributionsAndCandidatInfo(@PathVariable Long idUser) {
        List<Offre> offres = offreService.getOffresWithAttributionsAndCandidatInfo(idUser);
        if (offres.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/user/{userId}/{offreId}/matched-candidats")
    public ResponseEntity<?> getMatchedCandidatsForUserOffre(
            @PathVariable Long userId,
            @PathVariable Long offreId) {

        // Vérifie si l'utilisateur possède l'offre
        if (!offreService.isOffreOwnedByUser(offreId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This offer does not belong to the user.");
        }

        // Récupère les candidats matchés pour l'offre
        List<Candidat> matchedCandidats = offreService.getMatchedCandidatsForOffre(offreId);

        // Récupère les détails pour chaque candidat matché
        matchedCandidats.forEach(candidat -> {
            Candidat details = offreService.getCandidatDetails(candidat.getEmail());
            candidat.setDetails(details); // Assurez-vous que la classe Candidat a une méthode setDetails
        });

        return ResponseEntity.ok(matchedCandidats);
    }

    // create offre
    @PostMapping
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) {
        return ResponseEntity.ok(offreService.saveOrUpdateOffre(offre));
    }

    // update offre
    @PutMapping("/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offre) {
        // Ensure the ID is set to the path variable
        offre.setIdOffre(id);
        return ResponseEntity.ok(offreService.saveOrUpdateOffre(offre));
    }

    // delete offre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) {
        offreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
