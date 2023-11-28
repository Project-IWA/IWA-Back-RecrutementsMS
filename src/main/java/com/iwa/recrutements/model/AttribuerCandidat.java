package com.iwa.recrutements.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Entity
@Table(name = "attribuer_candidat")
@Data               // Generates getters, setters, toString, equals, and hashCode methods
@IdClass(AttribuerCandidatId.class) // Composite primary key
@Builder            // Provides a builder pattern for object creation
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields as arguments
public class AttribuerCandidat {

    @Id
    @Column(name = "id_offre")
    private Long idOffre;

    // une colonne email_candidat d'une entité faible qui n'est pas une table et dont l'id est l'email du candidat
    @Id
    @Column(name = "email_candidat")
    private String emailCandidat;

    @Transient
    private Candidat candidat; // Données récupérées du microservice "API Candidat"

    @Column(name = "note")
    private int note;

    @Column(name = "avis", length = 500)
    private String avis;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_offre", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Offre offre;

    // pas besoin de getters et setters car on utilise lombok
}
