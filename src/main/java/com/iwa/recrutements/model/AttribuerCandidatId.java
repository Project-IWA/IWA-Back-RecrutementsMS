package com.iwa.recrutements.model;

// Composite key class
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data               // Generates getters, setters, toString, equals, and hashCode methods
@Builder            // Provides a builder pattern for object creation
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields as arguments
public class AttribuerCandidatId implements Serializable {


    private Long idOffre;
    private String emailCandidat;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttribuerCandidatId)) return false;
        AttribuerCandidatId that = (AttribuerCandidatId) o;
        return Objects.equals(getIdOffre(), that.getIdOffre()) &&
                Objects.equals(getEmailCandidat(), that.getEmailCandidat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdOffre(), getEmailCandidat());
    }
}
