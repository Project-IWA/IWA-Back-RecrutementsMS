package com.iwa.recrutements.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
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
    @JsonProperty("id_offre")
    private Long idOffre;

    @Column(name = "emploi")
    private String emploi;

    @Column(name = "date_debut")
    @JsonProperty("date_debut")
    private Date dateDebut;

    @Column(name = "date_fin")
    @JsonProperty("date_fin")
    private Date dateFin;

    @Column(name = "salaire")
    private Double salaire;

    @Column(name = "avantages")
    private String avantages;

    @Column(name = "etat")
    private String etat;

    @Column(name = "nombre_candidats")
    @JsonProperty("nombre_candidats")
    private Integer nombreCandidats;

    @JsonManagedReference
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AttribuerCandidat> attributions;

    @Column(name = "id_user")
    @JsonProperty("id_user")
    private Long idUser;

    @Column(name = "id_etablissement")
    @JsonProperty("id_etablissement")
    private Long idEtablissement;

    // Getters, setters, constructors, etc.
    // pas besoin de getters et setters car on utilise lombok

}