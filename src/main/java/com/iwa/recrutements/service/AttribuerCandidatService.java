package com.iwa.recrutements.service;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.AttribuerCandidatId;
import com.iwa.recrutements.model.Candidat;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.repository.AttribuerCandidatRepository;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AttribuerCandidatService {

    private final RestTemplate restTemplate;

    @Value("${candidats.service.url}")
    private String candidatsServiceUrl;

    @Value("${matching.service.url}")
    private String matchingServiceUrl;

    private AttribuerCandidatRepository attribuerCandidatRepository;
    private OffreRepository offreRepository;

    @Autowired
    public AttribuerCandidatService(AttribuerCandidatRepository attribuerCandidatRepository, OffreRepository offreRepository, RestTemplate restTemplate) {
        this.attribuerCandidatRepository = attribuerCandidatRepository;
        this.offreRepository = offreRepository;
        this.restTemplate = restTemplate;
    }

    public List<AttribuerCandidat> getAllAttributions() {
        return attribuerCandidatRepository.findAll();
    }

    public List<AttribuerCandidat> getAttributionsByIdOffre(Long idOffre) {
        return attribuerCandidatRepository.findByIdOffre(idOffre);
    }

    public List<AttribuerCandidat> getAttributionsByEmailCandidat(String emailCandidat) {
        return attribuerCandidatRepository.findByEmailCandidat(emailCandidat);
    }

    @Transactional
    public AttribuerCandidat saveAttribution(AttribuerCandidat attribuerCandidat) {
        // Fetch the associated offer from the database
        Offre offre = offreRepository.findById(attribuerCandidat.getIdOffre())
                .orElseThrow(() -> new EntityNotFoundException("Offre not found"));

        Candidat candidat = getCandidatFromCandidatService(attribuerCandidat.getEmailCandidat());
        if (candidat == null) {
            throw new RuntimeException("Candidat with email " + attribuerCandidat.getEmailCandidat() + " not found");
        }

        // Supprimer les occurrences du candidat dans le microservice Matching
        restTemplate.delete(matchingServiceUrl + "/api/matching/delete-candidat/" + attribuerCandidat.getEmailCandidat());

        // Mettre à jour l'état du candidat dans le microservice Candidats
        Candidat candidatToUpdate = getCandidatFromCandidatService(attribuerCandidat.getEmailCandidat());
        if (candidatToUpdate != null) {
            candidatToUpdate.setEtat(Candidat.EtatCandidat.INDISPONIBLE);
            restTemplate.put(candidatsServiceUrl + "/api/candidats/update-state", candidatToUpdate);
        }

        // Check if the offer has enough space for a new candidate
        int currentNumberOfCandidates = attribuerCandidatRepository.countByIdOffre(offre.getIdOffre());
        if (currentNumberOfCandidates >= offre.getNombreCandidats()) {
            throw new IllegalStateException("The number of candidates for this offer has reached its limit.");
        }

        return attribuerCandidatRepository.save(attribuerCandidat);
    }

    private Candidat getCandidatFromCandidatService(String email) {
        final String candidatServiceUrl = candidatsServiceUrl + "/api/candidats/{email}";
        try {
            ResponseEntity<Candidat> response = restTemplate.getForEntity(candidatServiceUrl, Candidat.class, email);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            // Log l'erreur ou gère le cas où le candidat n'est pas trouvé
            System.out.println("Error retrieving candidat: " + e.getMessage());
        }
        return null;
    }

    @Transactional
    public void deleteAttribution(Long idOffre, String emailCandidat) {
        AttribuerCandidatId attribuerCandidatId = new AttribuerCandidatId(idOffre, emailCandidat);
        attribuerCandidatRepository.findById(attribuerCandidatId)
                .ifPresentOrElse(
                        attribuerCandidatRepository::delete,
                        () -> { throw new EntityNotFoundException("Attribution not found"); }
                );
    }


    // Autres méthodes selon vos besoins
}
