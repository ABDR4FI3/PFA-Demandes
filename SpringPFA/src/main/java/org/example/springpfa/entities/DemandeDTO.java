package org.example.springpfa.entities;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DemandeDTO {

    private String title;
    private String sujet;
    private Date date;
    private String etat;
}
