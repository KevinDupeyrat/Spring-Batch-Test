package com.test.batch.processor;

import com.test.batch.mapper.PlanningItemRowMapper;
import com.test.batch.model.Planning;
import com.test.batch.model.PlanningItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PlanningProcessor implements ItemProcessor<Planning, Planning> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY = "select f.libelle, s.date_debut, s.date_fin" +
            " from formations f join seances s on f.code=s.code_formation" +
            " where s.id_formateur=:formateur" +
            " order by s.date_debut";

    @Override
    public Planning process(Planning planning) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("formateur", planning.getFormateur().getId());
        List<PlanningItem> items = jdbcTemplate.query(QUERY, params, new PlanningItemRowMapper());
        planning.setSeances(items);
        return planning;
    }
}
