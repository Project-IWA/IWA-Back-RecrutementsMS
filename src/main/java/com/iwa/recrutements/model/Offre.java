package com.iwa.recrutements.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "offre")
@Data               // Generates getters, setters, toString, equals, and hashCode methods
@Builder            // Provides a builder pattern for object creation
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields as arguments
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offre")  // This maps the field to the id_offre column in the database
    private Long id_offre;

    @Column(name = "emploi")
    private String emploi;

    @Column(name = "date_debut")
    private Date date_debut;

    @Column(name = "date_fin")
    private Date date_fin;

    @Column(name = "salaire")
    private Double salaire;

    @Column(name = "avantages")
    private String avantages;

    @Column(name = "etat")
    private String etat;

    @Column(name = "nombre_candidats")
    private Integer nombre_candidats;

    @OneToMany(mappedBy = "offre")
    private Set<AttribuerCandidat> attributions;

    @Column(name = "id_user")
    private Long id_user;

    @Column(name = "id_etablissement")
    private Long id_etablissement;

    // Getters, setters, constructors, etc.
    // pas besoin de getters et setters car on utilise lombok

}