package com.iwa.recrutements.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidat {
    private String email;
    private String firstName;
    private String lastName;
    private int gender;
    private Date birthDate;
    private String citizenship;
    private String phone;
    private String photo; // URL de la photo
    private String cv; // URL du CV
    private String shortBio;
    private String experience; // l'experiance du candidat
    private EtatCandidat etat = EtatCandidat.DISPONIBLE; // L'état par défaut est "Disponible"

    // Enum pour définir les états possibles d'un candidat
    public enum EtatCandidat {
        DISPONIBLE,
        INDISPONIBLE
    }

    // Cette méthode prend en entrée un objet Candidat (probablement celui récupéré du microservice Candidat)
    // et met à jour les détails de l'instance actuelle avec ceux reçus.
    public void setDetails(Candidat details) {
        // Ici, nous mettons à jour les champs avec les valeurs de l'objet 'details'
        this.firstName = details.getFirstName();
        this.lastName = details.getLastName();
        this.gender = details.getGender();
        this.birthDate = details.getBirthDate();
        this.citizenship = details.getCitizenship();
        this.phone = details.getPhone();
        this.photo = details.getPhoto();
        this.cv = details.getCv();
        this.shortBio = details.getShortBio();
        // récupérer le job de expérience et le stocker dans la variable de classe experience
        this.experience = details.getExperience();
        this.etat = details.getEtat(); // Assurez-vous que l'objet 'details' a un champ 'etat' valide
    }

}

