package com.iwa.recrutements.service;

import com.iwa.recrutements.dto.OffrePostDTO;
import com.iwa.recrutements.dto.OffrePutDTO;
import com.iwa.recrutements.exception.MatchingServiceException;
import com.iwa.recrutements.exception.ResourceNotFoundException;
import com.iwa.recrutements.exception.TypeEmploiNotFoundException;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.model.TypeEmploi;
import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.Candidat;
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
    TypeEmploiService typeEmploiService;

    AttribuerCandidatRepository attribuerCandidatRepository;
    private final RestTemplate restTemplate;

    @Value("${matching.service.url}")
    private String matchingServiceUrl;

    @Value("${candidats.service.url}")
    private String candidatsServiceUrl;

    private MatchingUtilityService matchingUtilityService;

    @Autowired
    public OffreService(OffreRepository offreRepository,
                        TypeEmploiService typeEmploiService,
                        RestTemplateBuilder restTemplateBuilder,
                        AttribuerCandidatRepository attribuerCandidatRepository,
                        MatchingUtilityService matchingUtilityService) {
        this.offreRepository = offreRepository;
        this.typeEmploiService = typeEmploiService;
        this.attribuerCandidatRepository = attribuerCandidatRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.matchingUtilityService = matchingUtilityService;
    }

    // Get all offres
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    // Get all offres by user id
    public List<Offre> getAllOffresByUserId(Long userId) {
        return offreRepository.findAllByIdUser(userId);
    }

    // Get offre by id
    public Offre getOffreById(Long id) {
        return offreRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Offre with id " + id + " not found"));
    }


    public List<Offre> getOffresWithAttributionsAndCandidatInfo(Long idUser) {
        List<Offre> offres = offreRepository.findByIdUser(idUser);
        System.out.println("Offres: " + offres);
        offres.forEach(offre -> {
            List<AttribuerCandidat> attributions = attribuerCandidatRepository.findByIdOffre(offre.getIdOffre());
            Map<String, Candidat> candidatCache = new HashMap<>();

            attributions.forEach(attribution -> {
                Candidat candidat = candidatCache.computeIfAbsent(attribution.getEmailCandidat(), email -> {
                    try {
                        return restTemplate.getForObject(candidatsServiceUrl + "/api/candidats/" + email, Candidat.class);
                    } catch (RestClientException e) {
                        // Log l'erreur ou renvoie une réponse par défaut
                        System.out.println("Error retrieving candidat: " + e.getMessage());
                        return null; // Ou utiliser un objet Candidat par défaut/placeholder
                    }
                });
                System.out.println("Candidat: " + candidat);
                attribution.setCandidat(candidat);
            });
            offre.setAttributions(new HashSet<>(attributions));
            System.out.println("Offre: " + offre);
        });
        return offres;
    }

    // Create or Update offre
    public Offre saveOffre(Offre offre) {
        Offre savedOffre = offreRepository.save(offre);
        System.out.println("Saved offre: " + savedOffre);
        // After saving, trigger the matching process
        matchingUtilityService.triggerMatchingProcess();
        return savedOffre;
    }

    public Offre updateOffre(Offre offre) {
        Offre savedOffre = offreRepository.save(offre);
        System.out.println("updated offre: " + savedOffre);
        // After saving, trigger the matching process
        matchingUtilityService.triggerMatchingProcess();
        return savedOffre;
    }

    // Dans OffreService
    public boolean isOffreOwnedByUser(Long offreId, Long userId) {
        return offreRepository.findByIdOffreAndIdUser(offreId, userId).isPresent();
    }

    // Méthode dans OffreService pour appeler Matching Service
    public List<Candidat> getMatchedCandidatsForOffre(Long offreId) {
        String url = matchingServiceUrl + "/api/matching/get-matches/" + offreId;
        Candidat[] matchedCandidats = restTemplate.getForObject(url, Candidat[].class);
        return Arrays.asList(matchedCandidats);
    }

    public Candidat getCandidatDetails(String email) {
        String url = candidatsServiceUrl + "/api/candidats/" + email;
        return restTemplate.getForObject(url, Candidat.class, email);
    }

    // Delete offre
    public void delete(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre with id " + id + " not found");
        }
        // Appeler le microservice Matching pour supprimer toutes les correspondances de l'offre
        matchingUtilityService.removeMatchesByOffreId(id);
        // Supprimer l'offre
        offreRepository.deleteById(id);
    }

    public Offre mapPostDtoToEntity(OffrePostDTO offrePostDTO) {
        TypeEmploi typeEmploi = typeEmploiService.getTypeEmploiById(offrePostDTO.getIdTypeEmploi())
                .orElseThrow(() -> new TypeEmploiNotFoundException("Type emploi not found"));

        return Offre.builder()
                .emploi(offrePostDTO.getEmploi())
                .description(offrePostDTO.getDescription())
                .dateDebut(offrePostDTO.getDateDebut())
                .dateFin(offrePostDTO.getDateFin())
                .salaire(offrePostDTO.getSalaire())
                .avantages(offrePostDTO.getAvantages())
                .etat(offrePostDTO.getEtat())
                .nombreCandidats(offrePostDTO.getNombreCandidats())
                .idUser(offrePostDTO.getIdUser())
                .idEtablissement(offrePostDTO.getIdEtablissement())
                .typeEmploi(typeEmploi)
                .ville(offrePostDTO.getVille())
                .build();
    }

    public Offre mapUpdateDtoToEntity(OffrePutDTO offrePutDTO) {
        TypeEmploi typeEmploi = typeEmploiService.getTypeEmploiById(offrePutDTO.getIdTypeEmploi())
                .orElseThrow(() -> new TypeEmploiNotFoundException("Type emploi not found"));

        Offre offre = offreRepository.findById(offrePutDTO.getIdOffre()).orElseThrow(() ->
                new ResourceNotFoundException("Offre with id " + offrePutDTO.getIdOffre() + " not found"));

        offre.setEmploi(offrePutDTO.getEmploi());
        offre.setDescription(offrePutDTO.getDescription());
        offre.setDateDebut(offrePutDTO.getDateDebut());
        offre.setDateFin(offrePutDTO.getDateFin());
        offre.setSalaire(offrePutDTO.getSalaire());
        offre.setAvantages(offrePutDTO.getAvantages());
        offre.setEtat(offrePutDTO.getEtat());
        offre.setNombreCandidats(offrePutDTO.getNombreCandidats());
        offre.setIdUser(offrePutDTO.getIdUser());
        offre.setIdEtablissement(offrePutDTO.getIdEtablissement());
        offre.setTypeEmploi(typeEmploi);
        offre.setVille(offrePutDTO.getVille());

        return offre;

    }

}
