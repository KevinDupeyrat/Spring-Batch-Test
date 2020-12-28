package com.test.batch.mapper;

import com.test.batch.model.Formateur;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormateurItemPreparedStatementSetter implements ItemPreparedStatementSetter<Formateur> {

    public static final String ADD_FORMATEUR_QUERY = "INSERT INTO formateurs(id,nom,prenom,adresse_email) VALUES (?,?,?,?);";

    @Override
    public void setValues(Formateur formateur, PreparedStatement ps) throws SQLException {
        ps.setInt(1, formateur.getId());
        ps.setString(2, formateur.getNom());
        ps.setString(3, formateur.getPrenom());
        ps.setString(4, formateur.getAdresseEmail());
    }
}
