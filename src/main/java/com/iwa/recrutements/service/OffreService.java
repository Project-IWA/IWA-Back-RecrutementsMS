package com.iwa.recrutements.service;

import com.iwa.recrutements.dto.OffrePostDTO;
import com.iwa.recrutements.exception.ResourceNotFoundException;
import com.iwa.recrutements.exception.TypeEmploiNotFoundException;
import com.iwa.recrutements.model.Offre;
import com.iwa.recrutements.model.TypeEmploi;
import com.iwa.recrutements.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreService {

    OffreRepository offreRepository;
    TypeEmploiService typeEmploiService;

    @Autowired
    public OffreService(OffreRepository offreRepository,
                        TypeEmploiService typeEmploiService) {
        this.offreRepository = offreRepository;
        this.typeEmploiService = typeEmploiService;
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

    // Create offre
    public Offre save(Offre offre) {
        System.out.println("offre in service : " + offre);
        return offreRepository.save(offre);
    }

    // Delete offre
    public void delete(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre with id " + id + " not found");
        }
        offreRepository.deleteById(id);
    }

    public Offre mapDtoToEntity(OffrePostDTO offrePostDTO) {
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
                .build();
    }
}
