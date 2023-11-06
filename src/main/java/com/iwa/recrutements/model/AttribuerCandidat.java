package com.iwa.recrutements.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attribuer_candidat")
@Data               // Generates getters, setters, toString, equals, and hashCode methods
@Builder            // Provides a builder pattern for object creation
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields as arguments
public class AttribuerCandidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // une colonne email_candidat d'une entité faible qui n'est pas une table et dont l'id est l'email du candidat
    @Column(name = "email_candidat")
    private String emailCandidat;

    @ManyToOne
    @JoinColumn(name = "id_offre")
    private Offre offre;

    @Column(name = "note")
    private float note;

    @Column(name = "avis")
    private String avis;

    // Getters, setters, constructors, etc.
    // pas besoin de getters et setters car on utilise lombok

}
