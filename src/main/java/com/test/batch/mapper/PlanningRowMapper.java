package com.test.batch.mapper;

import com.test.batch.model.Formateur;
import com.test.batch.model.Planning;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanningRowMapper implements RowMapper<Planning> {

    @Override
    public Planning mapRow(ResultSet rs, int i) throws SQLException {
        return Planning.builder()
                .formateur(Formateur.builder()
                        .id(rs.getInt("id"))
                        .nom(rs.getString("nom"))
                        .prenom(rs.getString("prenom"))
                        .adresseEmail(rs.getString("adresse_email"))
                        .build())
                .build();
    }
}
