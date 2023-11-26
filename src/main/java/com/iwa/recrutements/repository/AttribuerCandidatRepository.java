package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.AttribuerCandidat;
import com.iwa.recrutements.model.AttribuerCandidatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttribuerCandidatRepository extends JpaRepository<AttribuerCandidat, AttribuerCandidatId> {
    List<AttribuerCandidat> findByIdOffre(Long idOffre);
    List<AttribuerCandidat> findByEmailCandidat(String emailCandidat);

    @Query("SELECT ac FROM AttribuerCandidat ac JOIN ac.offre o WHERE o.idUser = :userId")
    List<AttribuerCandidat> findAllByIdUser(@Param("userId") Long userId);

    int countByIdOffre(Long idOffre);

    // Other necessary query methods
}
