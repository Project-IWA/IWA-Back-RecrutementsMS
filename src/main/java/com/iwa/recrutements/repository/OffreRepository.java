package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    // Ici, vous pouvez ajouter des méthodes personnalisées si nécessaire
}
