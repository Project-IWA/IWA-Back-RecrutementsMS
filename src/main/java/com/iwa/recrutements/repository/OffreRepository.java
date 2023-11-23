package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    //
    List<Offre> findByIdUser(Long idUser);

    Optional<Offre> findByIdOffreAndIdUser(Long id, Long idUser);
}
