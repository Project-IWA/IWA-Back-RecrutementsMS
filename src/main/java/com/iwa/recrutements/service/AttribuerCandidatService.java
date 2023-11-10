package com.iwa.recrutements.service;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.repository.AttribuerCandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttribuerCandidatService {

    private AttribuerCandidatRepository attribuerCandidatRepository;

    @Autowired
    public AttribuerCandidatService(AttribuerCandidatRepository attribuerCandidatRepository) {
        this.attribuerCandidatRepository = attribuerCandidatRepository;
    }

    public List<AttribuerCandidat> getAllAttributions() {
        return attribuerCandidatRepository.findAll();
    }

    public Optional<AttribuerCandidat> getAttributionById(Long id) {
        return attribuerCandidatRepository.findById(id);
    }

    public AttribuerCandidat saveOrUpdateAttribution(AttribuerCandidat attribuerCandidat) {
        return attribuerCandidatRepository.save(attribuerCandidat);
    }

    public void deleteAttribution(Long id) {
        attribuerCandidatRepository.deleteById(id);
    }

    // Autres méthodes selon vos besoins
}
