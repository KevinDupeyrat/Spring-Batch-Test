package com.test.batch.step;

import com.test.batch.listener.ChargementFormateurStepListener;
import com.test.batch.mapper.FormateurItemPreparedStatementSetter;
import com.test.batch.model.Formateur;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
public class ChargementFormateurStepConfig {


    @Bean
    @StepScope
    public FlatFileItemReader<Formateur> formateurItemReader(
            @Value("#{jobParameters['formateursFile']}") final Resource inputFile) {
        return new FlatFileItemReaderBuilder<Formateur>()
                .name("formateurItemReader")
                .resource(inputFile)
                .delimited()
                .delimiter(";")
                .names("id", "nom", "prenom", "adresseEmail")
                .targetType(Formateur.class)
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Formateur> formateurItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Formateur>()
                .dataSource(dataSource)
                .sql(FormateurItemPreparedStatementSetter.ADD_FORMATEUR_QUERY)
                .itemPreparedStatementSetter(new FormateurItemPreparedStatementSetter())
                .build();
    }


    @Bean
    public Step chargementFormateurStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("chargementFormateurStep")
                .<Formateur, Formateur>chunk(2)
                .reader(formateurItemReader(null))
                .writer(formateurItemWriter(null))
                .listener(chargementFormateurStepListener())
                .build();
    }


    @Bean
    public StepExecutionListener chargementFormateurStepListener() {
        return new ChargementFormateurStepListener();
    }
}
