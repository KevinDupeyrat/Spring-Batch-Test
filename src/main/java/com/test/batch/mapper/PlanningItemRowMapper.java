package com.test.batch.mapper;

import com.test.batch.model.PlanningItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanningItemRowMapper implements RowMapper<PlanningItem> {

    @Override
    public PlanningItem mapRow(ResultSet rs, int i) throws SQLException {
        return PlanningItem.builder()
                .libelleFormation(rs.getString(1))
                .dateDebutFormation(rs.getDate(2).toLocalDate())
                .dateFinFormation(rs.getDate(3).toLocalDate())
                .build();
    }
}
