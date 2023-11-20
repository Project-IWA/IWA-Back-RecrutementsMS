package com.iwa.recrutements.service;

import com.iwa.recrutements.model.TypeEmploi;
import com.iwa.recrutements.repository.TypeEmploiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeEmploiService {

    private TypeEmploiRepository typeEmploiRepository;

    @Autowired
    public TypeEmploiService(TypeEmploiRepository typeEmploiRepository) {
        this.typeEmploiRepository = typeEmploiRepository;
    }

    // Get all typeEmplois
    public List<TypeEmploi> getAllTypeEmplois() {
        return typeEmploiRepository.findAll();
    }

    // Get typeEmploi by id, returns an Optional<TypeEmploi>
    public Optional<TypeEmploi> getTypeEmploiById(Long id) {
        return typeEmploiRepository.findById(id);
    }
}
