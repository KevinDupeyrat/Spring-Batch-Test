package com.test.batch.mapper;

import com.test.batch.model.Formation;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormationItemPreparedStatementSetter implements ItemPreparedStatementSetter<Formation> {

    public static final String ADD_FORMATION_QUERY = "INSERT INTO formations(code,libelle,descriptif) VALUES (?,?,?);";

    @Override
    public void setValues(Formation formateur, PreparedStatement ps) throws SQLException {
        ps.setString(1, formateur.getCode());
        ps.setString(2, formateur.getLibelle());
        ps.setString(3, formateur.getDescriptif());
    }
}
