package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;


@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findAllByIdUser(Long userId);
    // Ici, vous pouvez ajouter des méthodes personnalisées si nécessaire

    List<Offre> findByIdUser(Long idUser);

    Optional<Offre> findByIdOffreAndIdUser(Long id, Long idUser);

}
