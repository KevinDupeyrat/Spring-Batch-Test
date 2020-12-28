package com.test.batch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanningItem {

    private String libelleFormation;
    private String descriptifFormation;
    private LocalDate dateDebutFormation;
    private LocalDate dateFinFormation;
}
