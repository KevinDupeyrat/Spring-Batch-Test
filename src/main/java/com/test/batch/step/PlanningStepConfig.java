package com.test.batch.step;

import com.test.batch.mapper.PlanningRowMapper;
import com.test.batch.model.Planning;
import com.test.batch.processor.PlanningProcessor;
import com.test.batch.service.contract.IMailContentGenerator;
import com.test.batch.service.contract.IPlanningMailSenderService;
import com.test.batch.service.impl.MailContentGenerator;
import com.test.batch.service.impl.PlanningMailSenderService;
import com.test.batch.writer.PlanningItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class PlanningStepConfig {


    @Bean
    public JdbcCursorItemReader<Planning> planningItemReader(final DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Planning>()
                .name("planningItemReader")
                .dataSource(dataSource)
                .sql("select distinct f.* from formateurs f join seances s on f.id=s.id_formateur")
                .rowMapper(new PlanningRowMapper())
                .build();
    }


    @Bean
    public ItemProcessor<Planning, Planning> planningItemProcessor(final NamedParameterJdbcTemplate jdbcTemplate) {
        return new PlanningProcessor(jdbcTemplate);
    }


    @Bean
    public PlanningItemWriter planningItemWriter(final IMailContentGenerator mailContentGenerator,
                                                 final IPlanningMailSenderService planningMailSenderService) {
        return new PlanningItemWriter(mailContentGenerator, planningMailSenderService);
    }


    @Bean
    public IMailContentGenerator mailContentGenerator(final freemarker.template.Configuration configuration)
            throws IOException {
        return new MailContentGenerator(configuration);
    }

    @Bean
    public IPlanningMailSenderService planningMailSenderService(final JavaMailSender javaMailSender) {
        return new PlanningMailSenderService(javaMailSender);
    }


    @Bean
    public Step planningStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("planningStep")
                .<Planning, Planning>chunk(10)
                .reader(planningItemReader(null))
                .processor(planningItemProcessor(null))
                .writer(planningItemWriter(null, null))
                .build();

    }

}
