package com.iwa.recrutements.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OffrePostDTO {

    private String emploi;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private Double salaire;
    private String avantages;
    private String etat;
    private Integer nombreCandidats;
    private Long idUser;
    private Long idEtablissement;
    private Long idTypeEmploi; // Add this to reference the TypeEmploi by ID

    // Getters and setters
}
