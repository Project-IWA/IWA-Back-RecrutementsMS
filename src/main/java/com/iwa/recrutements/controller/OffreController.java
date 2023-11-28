package com.iwa.recrutements.controller;

import com.iwa.recrutements.dto.OffrePostDTO;
import com.iwa.recrutements.exception.DateDebutAfterDateFinException;
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

    // Get all offres from db
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
    @GetMapping("/user")
    public ResponseEntity<List<Offre>> getOffresWithAttributionsAndCandidatInfo(@RequestHeader("AuthUserId") Long userId) {
        List<Offre> offres = offreService.getOffresWithAttributionsAndCandidatInfo(userId);
        if (offres.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offres);
    }

    @GetMapping("matched-candidats/{offreId}")
    public ResponseEntity<?> getMatchedCandidatsForUserOffre(
            @RequestHeader("AuthUserId") Long userId,
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
    // Only the user that sent the request can create an offre with his id
    @PostMapping
    public ResponseEntity<Offre> createOffre(@RequestBody OffrePostDTO offrePostDTO,
                                             @RequestHeader("AuthUserId") Long userId) {
        // check if the user id in the header is the same as the user id in the body
        // If not, set the user id in the body to the user id in the header
        if (userId != offrePostDTO.getIdUser()) {
            System.out.println("userId" + userId);
            System.out.println("offrePostDTO.getIdUser()" + offrePostDTO.getIdUser());
            offrePostDTO.setIdUser(userId);
            System.out.println("offrePostDTO.getIdUser()" + offrePostDTO.getIdUser());
        }

        // check that dateDebut of offre is before dateFin of offre
        if (offrePostDTO.getDateDebut().after(offrePostDTO.getDateFin())) {
            throw new DateDebutAfterDateFinException(offrePostDTO.getDateDebut(), offrePostDTO.getDateFin());
        }
        Offre offre = offreService.mapDtoToEntity(offrePostDTO);
        System.out.println("offre in controller : " + offre);
        // save and return 204 created
        Offre offreCreated = offreService.saveOrUpdateOffre(offre);
        return new ResponseEntity<>(offreCreated, HttpStatus.CREATED);
    }

    // update offre
    // Only the user that sent the request can update his offre
    @PutMapping("/{idOffre}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long idOffre,
                                             @RequestBody OffrePostDTO offrePostDTO,
                                             @RequestHeader("AuthUserId") Long userId) {
        // check if the user id in the header is the same as the user id in the body
        // If not, bad request
        if (userId != offrePostDTO.getIdUser()) {
            return ResponseEntity.badRequest().build();
        }

        // Map the dto to offre model
        Offre offre = offreService.mapDtoToEntity(offrePostDTO);

        // Ensure the ID is set to the path variable
        offre.setIdOffre(idOffre);
        return ResponseEntity.ok(offreService.saveOrUpdateOffre(offre));
    }

    // delete offre
    // Only the user that sent the request can delete his offre
    @DeleteMapping("/{idOffre}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long idOffre,
                                            @RequestHeader("AuthUserId") Long userId) {
        // check if the user id in the header is the same as the user id in the body
        // If not, bad request
        Offre offre = offreService.getOffreById(idOffre);
        if (userId != offre.getIdUser()) {
            return ResponseEntity.badRequest().build();
        }
        offreService.delete(idOffre);
        return ResponseEntity.noContent().build();
    }
}
