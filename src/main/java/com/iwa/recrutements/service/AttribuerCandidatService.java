package com.iwa.recrutements.service;

import com.iwa.recrutements.exception.CandidatNotFoundException;
import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.AttribuerCandidatId;
import com.iwa.recrutements.model.Candidat;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.repository.AttribuerCandidatRepository;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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

    private MatchingUtilityService matchingUtilityService;

    @Autowired
    public AttribuerCandidatService(AttribuerCandidatRepository attribuerCandidatRepository,
                                    OffreRepository offreRepository,
                                    RestTemplate restTemplate,
                                    MatchingUtilityService matchingUtilityService) {
        this.attribuerCandidatRepository = attribuerCandidatRepository;
        this.offreRepository = offreRepository;
        this.restTemplate = restTemplate;
        this.matchingUtilityService = matchingUtilityService;
    }

    public List<AttribuerCandidat> getAllAttributions() {
        return attribuerCandidatRepository.findAll();
    }

    // get all attributions by idOffre, which means the attributions that have all the offres that has the id of the user in the header
    public List<AttribuerCandidat> getAttributionsByIdOffre(Long idOffre) {
        return attribuerCandidatRepository.findByIdOffre(idOffre);
    }

    public List<AttribuerCandidat> getAttributionsByEmailCandidat(String emailCandidat) {
        return attribuerCandidatRepository.findByEmailCandidat(emailCandidat);
    }

    // get all attributions by idUser, which means the attributions that have the offre that has the id of the user in the header
    public List<AttribuerCandidat> getAllAttributionsByUserId(Long userId) {
        return attribuerCandidatRepository.findAllByIdUser(userId);
    }

    public AttribuerCandidat saveAttributionTest(AttribuerCandidat attribuerCandidat) {
        return attribuerCandidatRepository.save(attribuerCandidat);
    }

    @Transactional
    public AttribuerCandidat saveAttribution(AttribuerCandidat attribuerCandidat) {
        // Fetch the associated offer from the database
        Offre offre = offreRepository.findById(attribuerCandidat.getIdOffre())
                .orElseThrow(() -> new EntityNotFoundException("Offre not found"));

        // Check if the offer has enough space for a new candidate
        int currentNumberOfCandidates = attribuerCandidatRepository.countByIdOffre(offre.getIdOffre());
        if (currentNumberOfCandidates >= offre.getNombreCandidats()) {
            throw new IllegalStateException("The number of candidates for this offer has reached its limit.");
        }

        // Check if the candidat exists in the Candidats microservice
        Candidat candidat = getCandidatFromCandidatService(attribuerCandidat.getEmailCandidat());
        if (candidat == null) {
            throw new CandidatNotFoundException("Candidat with email " + attribuerCandidat.getEmailCandidat() + " not found");
        }

        // S'assurer de supprimer les occurrences du candidat dans le microservice Matching
        restTemplate.delete(matchingServiceUrl + "/api/matching/delete-candidat/" + attribuerCandidat.getEmailCandidat());
        System.out.println("[+] Candidat" + attribuerCandidat.getEmailCandidat()+ " deleted from Matching");

        // S'assurer de mettre à jour l'état du candidat dans le microservice Candidats en tant que INDISPONIBLE
        System.out.println("[+] Updating Candidat" + attribuerCandidat.getEmailCandidat()+ " to INDISPONIBLE in Candidats");
        String etatToUpdate = Candidat.EtatCandidat.INDISPONIBLE.name();
        String updateUrl = candidatsServiceUrl + "/api/candidats/update-state/" + candidat.getEmail() + "?etat=" + etatToUpdate;
        restTemplate.put(updateUrl, null);
        System.out.println("[+] Candidat" + attribuerCandidat.getEmailCandidat() + "updated to DISPONIBLE in Candidats");

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
    public AttribuerCandidat updateAttribution(AttribuerCandidat attribuerCandidat) {
        // Fetch the associated offer from the database
        Offre offre = offreRepository.findById(attribuerCandidat.getIdOffre())
                .orElseThrow(() -> new EntityNotFoundException("Offre not found"));

        // Check if the candidat exists in the Candidats microservice
        Candidat candidat = getCandidatFromCandidatService(attribuerCandidat.getEmailCandidat());
        if (candidat == null) {
            throw new CandidatNotFoundException("Candidat with email " + attribuerCandidat.getEmailCandidat() + " not found");
        }

        // S'assurer de supprimer les occurrences du candidat dans le microservice Matching
        restTemplate.delete(matchingServiceUrl + "/api/matching/delete-candidat/" + attribuerCandidat.getEmailCandidat());
        System.out.println("[+] Candidat" + attribuerCandidat.getEmailCandidat()+ " deleted from Matching");

        // S'assurer de mettre à jour l'état du candidat dans le microservice Candidats en tant que INDISPONIBLE
        System.out.println("[+] Updating Candidat" + attribuerCandidat.getEmailCandidat()+ " to INDISPONIBLE in Candidats");
        String etatToUpdate = Candidat.EtatCandidat.INDISPONIBLE.name();
        String updateUrl = candidatsServiceUrl + "/api/candidats/update-state/" + candidat.getEmail() + "?etat=" + etatToUpdate;
        restTemplate.put(updateUrl, null);
        System.out.println("[+] Candidat" + attribuerCandidat.getEmailCandidat() + "updated to DISPONIBLE in Candidats");

        return attribuerCandidatRepository.save(attribuerCandidat);
    }

    @Transactional
    public void deleteAttribution(Long idOffre, String emailCandidat) {
        // Fetch the associated offer from the database
        Offre offre = offreRepository.findById(idOffre)
                .orElseThrow(() -> new EntityNotFoundException("Offre not found"));

        // Check if the candidat exists in the Candidats microservice
        Candidat candidat = getCandidatFromCandidatService(emailCandidat);
        if (candidat == null) {
            throw new CandidatNotFoundException("[-] Candidat with email " + emailCandidat + " not found");
        }

        // Mettre à jour l'état du candidat dans le microservice Candidats
        System.out.println("[+] Updating Candidat" + emailCandidat + " to DISPONIBLE in Candidats");
        String etatToUpdate = Candidat.EtatCandidat.DISPONIBLE.name();
        String updateUrl = candidatsServiceUrl + "/api/candidats/update-state/" + candidat.getEmail() + "?etat=" + etatToUpdate;
        restTemplate.put(updateUrl, null);
        System.out.println("[+] Candidat" + emailCandidat + "updated to DISPONIBLE in Candidats");

        // supprimer attribution
        AttribuerCandidatId attribuerCandidatId = new AttribuerCandidatId(idOffre, emailCandidat);
        attribuerCandidatRepository.findById(attribuerCandidatId)
                .ifPresentOrElse(
                        attribuerCandidatRepository::delete,
                        () -> { throw new EntityNotFoundException("Attribution not found"); }
                );

        // Execute matching process
        System.out.println("[+] Triggering matching process after deleting attribution");
        matchingUtilityService.triggerMatchingProcess();
    }





    // Autres méthodes selon vos besoins
}
