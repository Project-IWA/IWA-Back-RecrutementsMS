package com.iwa.recrutements.repository;

import com.iwa.recrutements.model.TypeEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeEmploiRepository extends JpaRepository<TypeEmploi, Long> {

}
