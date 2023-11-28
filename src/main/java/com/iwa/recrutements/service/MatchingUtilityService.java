package com.iwa.recrutements.service;

import com.iwa.recrutements.exception.MatchingServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MatchingUtilityService {

    private final RestTemplate restTemplate;

    @Value("${matching.service.url}")
    private String matchingServiceUrl;

    @Autowired
    public MatchingUtilityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to trigger the matching process
    protected void triggerMatchingProcessForOffre(Long offreId) {
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

    protected void triggerMatchingProcess() {
        // Construct the URL to call the Matching service
        String executeMatchingUrl = matchingServiceUrl + "/api/matching/execute";

        // Create a request entity if needed, otherwise use null
        HttpEntity<?> requestEntity = new HttpEntity<>(null);

        // Make the HTTP call to the Matching service
        ResponseEntity<String> response = restTemplate.postForEntity(executeMatchingUrl, requestEntity, String.class);

        // Check the response status and handle it accordingly
        if (!response.getStatusCode().is2xxSuccessful()) {
            // Handle the error
            System.out.println("[-] Error triggering the matching process: " + response.getStatusCode());
        } else {
            System.out.println("[+] Matching process triggered successfully: " + response.getBody());
        }
    }

    protected void removeMatchesByOffreId(Long idOffre) {
        String url = matchingServiceUrl + "/api/matching/remove-matches/" + idOffre;
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            // Log l'erreur ou gère le cas où l'appel échoue
            throw new MatchingServiceException("Failed to remove matches for offre id: " + idOffre, e);
        }

    }
}
