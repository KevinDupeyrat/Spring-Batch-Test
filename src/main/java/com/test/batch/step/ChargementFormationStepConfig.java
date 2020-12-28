package com.test.batch.step;

import com.test.batch.listener.ChargementFormationStepListener;
import com.test.batch.mapper.FormationItemPreparedStatementSetter;
import com.test.batch.model.Formation;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@Configuration
public class ChargementFormationStepConfig {


    @Bean
    @StepScope
    public StaxEventItemReader<Formation> formationItemReader(
            @Value("#{jobParameters['formationsFile']}") final Resource inputFile) {
        return new StaxEventItemReaderBuilder<Formation>()
                .name("formationItemReader")
                .resource(inputFile)
                .addFragmentRootElements("formation")
                .unmarshaller(formationMarshaller())
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Formation> formationItemWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Formation>()
                .dataSource(dataSource)
                .sql(FormationItemPreparedStatementSetter.ADD_FORMATION_QUERY)
                .itemPreparedStatementSetter(new FormationItemPreparedStatementSetter())
                .build();
    }


    @Bean
    public Jaxb2Marshaller formationMarshaller() {
        Class [] classes = new Class[1];
        classes[0] = Formation.class;
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(classes);

        return jaxb2Marshaller;
    }


    @Bean
    public Step chargementFormationStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("chargementFormationStep")
                .<Formation, Formation>chunk(2)
                .reader(formationItemReader(null))
                .writer(formationItemWriter(null))
                .listener(chargementFormationStepListener())
                .build();
    }


    @Bean
    public StepExecutionListener chargementFormationStepListener() {
        return new ChargementFormationStepListener();
    }
}
