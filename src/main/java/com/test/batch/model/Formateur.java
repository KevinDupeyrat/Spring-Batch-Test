package com.test.batch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Formateur {

    private Integer id;
    private String nom;
    private String prenom;
    private String adresseEmail;
}
