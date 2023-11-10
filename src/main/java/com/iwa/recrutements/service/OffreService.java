package com.iwa.recrutements.service;

import com.iwa.recrutements.exception.ResourceNotFoundException;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreService {

    OffreRepository offreRepository;

    @Autowired
    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
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

    // Create offre
    public Offre save(Offre offre) {
        return offreRepository.save(offre);
    }

    // Delete offre
    public void delete(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre with id " + id + " not found");
        }
        offreRepository.deleteById(id);
    }
}
