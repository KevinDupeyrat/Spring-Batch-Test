package com.test.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seance {

    private String codeFormation;
    private Integer idFormateur;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
