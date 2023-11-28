package com.iwa.recrutements.controller;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.service.AttribuerCandidatService;
import com.iwa.recrutements.service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributions")
public class AttribuerCandidatController {

    private AttribuerCandidatService attribuerCandidatService;
    private OffreService offreService;

    @Autowired
    public AttribuerCandidatController(AttribuerCandidatService attribuerCandidatService,
                                       OffreService offreService) {
        this.attribuerCandidatService = attribuerCandidatService;
        this.offreService = offreService;
    }

    // Get all attributions for the user with the id in the header
    @GetMapping
    public ResponseEntity<List<AttribuerCandidat>> getAllAttributions(@RequestHeader("AuthUserId") Long userId) {
        return ResponseEntity.ok(attribuerCandidatService.getAllAttributionsByUserId(userId));
    }

    // Get attribution by id offre for the user with the id in the header
    @GetMapping("/offre/{idOffre}")
    public ResponseEntity<List<AttribuerCandidat>> getAttributionsByIdOffre(@PathVariable Long idOffre,
                                                                             @RequestHeader("AuthUserId") Long userId) {
        // check if the user id in the header is the same as the user id in the body
        // If not, bad request
        Offre offre = offreService.getOffreById(idOffre);
        if (offre.getIdUser() != userId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(attribuerCandidatService.getAttributionsByIdOffre(idOffre));
    }

    // Get attribution by email candidat for the user with the id in the header
    @GetMapping("/candidat/{emailCandidat}")
    public ResponseEntity<List<AttribuerCandidat>> getAttributionsByEmailCandidat(@PathVariable String emailCandidat,
                                                                                   @RequestHeader("AuthUserId") Long userId) {
        // get all attributions by email candidat then keep only the attributions that have the offre that has the id of the user in the header
        List<AttribuerCandidat> attribuerCandidats = attribuerCandidatService.getAttributionsByEmailCandidat(emailCandidat);
        // now we have all the attributions by email candidat, we need to keep only the attributions that have the offre that has the id of the user in the header
        // we can do that by checking if the id of the user in the header is the same as the id of the user in the offre
        // if not, we remove the attribution from the list
        attribuerCandidats.removeIf(attribuerCandidat -> attribuerCandidat.getOffre().getIdUser() != userId);
        return ResponseEntity.ok(attribuerCandidats);
    }

    // Create attribution only for the offre owner, which means the offre that has the id of the user in the header
    @PostMapping
    public ResponseEntity<AttribuerCandidat> createAttribution(@RequestBody AttribuerCandidat attribuerCandidat,
                                                                @RequestHeader("AuthUserId") Long userId) {
        Offre offre = offreService.getOffreById(attribuerCandidat.getIdOffre());
        if (offre.getIdUser() != userId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(attribuerCandidatService.saveAttribution(attribuerCandidat));
    }

    // Update attribution only for the offre owner, which means the offre that has the id of the user in the header
    @PutMapping
    public ResponseEntity<AttribuerCandidat> updateAttribution(@RequestBody AttribuerCandidat attribuerCandidat,
                                                               @RequestHeader("AuthUserId") Long userId) {
        Offre offre = offreService.getOffreById(attribuerCandidat.getIdOffre());
        if (offre.getIdUser() != userId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(attribuerCandidatService.updateAttribution(attribuerCandidat));
    }

    // delete attribution by id offre and email candidat only for the offre owner, which means the offre that has the id of the user in the header
    @DeleteMapping("/{idOffre}/{emailCandidat}")
    public ResponseEntity<Void> deleteAttribution(@PathVariable Long idOffre, @PathVariable String emailCandidat,
                                                  @RequestHeader("AuthUserId") Long userId) {
        Offre offre = offreService.getOffreById(idOffre);
        if (offre.getIdUser() != userId) {
            return ResponseEntity.badRequest().build();
        }
        attribuerCandidatService.deleteAttribution(idOffre, emailCandidat);
        return ResponseEntity.ok().build();
    }

    // Autres endpoints
}
