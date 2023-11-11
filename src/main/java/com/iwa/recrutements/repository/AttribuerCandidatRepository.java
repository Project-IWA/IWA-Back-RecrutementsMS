package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.AttribuerCandidatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttribuerCandidatRepository extends JpaRepository<AttribuerCandidat, AttribuerCandidatId> {
    List<AttribuerCandidat> findByIdOffre(Long idOffre);
    List<AttribuerCandidat> findByEmailCandidat(String emailCandidat);
    // Other necessary query methods
}
