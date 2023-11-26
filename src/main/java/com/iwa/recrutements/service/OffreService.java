package com.iwa.recrutements.service;

import com.iwa.recrutements.exception.MatchingServiceException;
import com.iwa.recrutements.exception.ResourceNotFoundException;
import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.Candidat;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.repository.AttribuerCandidatRepository;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OffreService {

    OffreRepository offreRepository;

    AttribuerCandidatRepository attribuerCandidatRepository;
    private final RestTemplate restTemplate;

    @Value("${matching.service.url}")
    private String matchingServiceUrl;

    @Value("${candidats.service.url}")
    private String candidatsServiceUrl;

    @Autowired
    public OffreService(OffreRepository offreRepository, RestTemplateBuilder restTemplateBuilder) {
        this.offreRepository = offreRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    // Get all offres
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    // Get offre by id
    public Offre getOffreById(Long id) {
        return offreRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Offre with id " + id + " not found"));
    }

    public List<Offre> getOffresWithAttributionsAndCandidatInfo(Long idUser) {
        List<Offre> offres = offreRepository.findByIdUser(idUser);
        offres.forEach(offre -> {
            List<AttribuerCandidat> attributions = attribuerCandidatRepository.findByIdOffre(offre.getIdOffre());
            Map<String, Candidat> candidatCache = new HashMap<>();

            attributions.forEach(attribution -> {
                Candidat candidat = candidatCache.computeIfAbsent(attribution.getEmailCandidat(), email -> {
                    try {
                        return restTemplate.getForObject(candidatsServiceUrl + '/' + email, Candidat.class);
                    } catch (RestClientException e) {
                        // Log l'erreur ou renvoie une réponse par défaut
                        System.out.println("Error retrieving candidat: " + e.getMessage());
                        return null; // Ou utiliser un objet Candidat par défaut/placeholder
                    }
                });
                attribution.setCandidat(candidat);
            });
            offre.setAttributions(new HashSet<>(attributions));
        });
        return offres;
    }

    // Create or Update offre
    public Offre saveOrUpdateOffre(Offre offre) {
        Offre savedOffre = offreRepository.save(offre);
        // After saving, trigger the matching process
        triggerMatchingProcess(savedOffre.getIdOffre());
        return savedOffre;
    }

    // Method to trigger the matching process
    private void triggerMatchingProcess(Long offreId) {
        // Construct the URL to call the Matching service
        String executeMatchingUrl = matchingServiceUrl + "/api/matching/execute";

        // Make the HTTP call to the Matching service
        ResponseEntity<String> response = restTemplate.postForEntity(executeMatchingUrl, offreId, String.class);

        // Check the response status and handle it accordingly
        if (!response.getStatusCode().is2xxSuccessful()) {
            // Handle the error
            System.out.println("Error triggering the matching process: " + response.getStatusCode());
        }
    }

    // Dans OffreService
    public boolean isOffreOwnedByUser(Long offreId, Long userId) {
        return offreRepository.findByIdOffreAndIdUser(offreId, userId).isPresent();
    }

    // Méthode dans OffreService pour appeler Matching Service
    public List<Candidat> getMatchedCandidatsForOffre(Long offreId) {
        String url = matchingServiceUrl + "/api/matching/execute/" + offreId;
        Candidat[] matchedCandidats = restTemplate.getForObject(url, Candidat[].class);
        return Arrays.asList(matchedCandidats);
    }

    public Candidat getCandidatDetails(String email) {
        String url = candidatsServiceUrl + "/api/candidats/{email}";
        return restTemplate.getForObject(url, Candidat.class, email);
    }

    // Delete offre
    public void delete(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre with id " + id + " not found");
        }
        // Appeler le microservice Matching pour supprimer toutes les correspondances de l'offre
        removeMatchesByOffreId(id);
        // Supprimer l'offre
        offreRepository.deleteById(id);
    }

    private void removeMatchesByOffreId(Long idOffre) {
        String url = matchingServiceUrl + "/api/matching/remove-matches/" + idOffre;
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            // Log l'erreur ou gère le cas où l'appel échoue
            throw new MatchingServiceException("Failed to remove matches for offre id: " + idOffre, e);
        }
    }
}
