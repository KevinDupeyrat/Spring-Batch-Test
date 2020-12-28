package com.test.batch.mapper;

import com.test.batch.model.Seance;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeanceItemPreparedStatementSetter implements ItemPreparedStatementSetter<Seance> {

    public static final String ADD_SCEANCE_QUERY = "INSERT INTO seances (code_formation, id_formateur, date_debut, date_fin) VALUES (?,?,?,?);";

    @Override
    public void setValues(Seance seance, PreparedStatement ps) throws SQLException {
        ps.setString(1, seance.getCodeFormation());
        ps.setInt(2, seance.getIdFormateur());
        ps.setDate(3, Date.valueOf(seance.getDateDebut()));
        ps.setDate(4, Date.valueOf(seance.getDateFin()));
    }
}
