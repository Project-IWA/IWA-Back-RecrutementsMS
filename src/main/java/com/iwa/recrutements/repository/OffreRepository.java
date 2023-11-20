package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    //
    List<Offre> findByIdUser(Long idUser);
}
