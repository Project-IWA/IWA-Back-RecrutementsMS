package com.iwa.recrutements.controller;

import com.iwa.recrutements.dto.OffrePostDTO;
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

    // Get all offres for the user with the id in the header
    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffres(@RequestHeader("AuthUserId") Long userId) {
return ResponseEntity.ok(offreService.getAllOffresByUserId(userId));
    }

    // Get offre by id
    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.getOffreById(id));
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
        Offre offre = offreService.mapDtoToEntity(offrePostDTO);
        System.out.println("offre in controller : " + offre);
        // save and return 204 created
        Offre offreCreated = offreService.save(offre);
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
        return ResponseEntity.ok(offreService.save(offre));
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
