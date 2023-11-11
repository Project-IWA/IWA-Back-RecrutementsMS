package com.iwa.recrutements.service;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.AttribuerCandidatId;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.repository.AttribuerCandidatRepository;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AttribuerCandidatService {

    private AttribuerCandidatRepository attribuerCandidatRepository;
    private OffreRepository offreRepository;

    @Autowired
    public AttribuerCandidatService(AttribuerCandidatRepository attribuerCandidatRepository, OffreRepository offreRepository) {
        this.attribuerCandidatRepository = attribuerCandidatRepository;
        this.offreRepository = offreRepository;
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
        Offre offre = offreRepository.findById(attribuerCandidat.getIdOffre()).orElseThrow(() -> new EntityNotFoundException("Offre not found"));
        return attribuerCandidatRepository.save(attribuerCandidat);
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
