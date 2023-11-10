package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.AttribuerCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttribuerCandidatRepository extends JpaRepository<AttribuerCandidat, Long> {
    // Ici, vous pouvez ajouter des méthodes personnalisées si nécessaire
}
