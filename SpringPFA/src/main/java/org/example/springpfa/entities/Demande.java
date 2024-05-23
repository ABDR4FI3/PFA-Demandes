package org.example.springpfa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    private String title;
    @Lob
    private String sujet;
    private Date date;
    private String etat;

    // Remove this reference
     @ManyToOne(fetch = FetchType.LAZY)
     @JsonBackReference
     @JoinColumn(name = "user_id")
        private User user;

    // constructors, getters, setters, etc.
}
