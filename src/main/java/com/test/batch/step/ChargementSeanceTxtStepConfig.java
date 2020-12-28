package com.test.batch.step;

import com.test.batch.listener.ChargementSeanceStepListener;
import com.test.batch.mapper.SeanceItemPreparedStatementSetter;
import com.test.batch.model.Seance;
import com.test.batch.policy.SeanceSkipPolicy;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ChargementSeanceTxtStepConfig {


    /**
     * TXT
     *
     * @param inputFile
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Seance> seanceTxtItemReader(
            @Value("#{jobParameters['seancesFile']}") final Resource inputFile) {
        return new FlatFileItemReaderBuilder<Seance>()
                .name("seanceTxtItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(new Range(1, 15), new Range(16, 19), new Range(24, 31), new Range(36, 43))
                .names("codeFormation", "idFormateur", "dateDebut", "dateFin")
                .fieldSetMapper(seanceFieldSetMapper(null))
                .build();
    }


    /**
     * CSV
     *
     * @param inputFile
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Seance> seanceCsvItemReader(
            @Value("#{jobParameters['seancesFile']}") final Resource inputFile) {
        return new FlatFileItemReaderBuilder<Seance>()
                .name("formateurItemReader")
                .resource(inputFile)
                .delimited()
                .delimiter(";")
                .names("codeFormation", "idFormateur", "dateDebut", "dateFin")
                .fieldSetMapper(seanceFieldSetMapper(null))
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Seance> sceanceItemWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Seance>()
                .dataSource(dataSource)
                .sql(SeanceItemPreparedStatementSetter.ADD_SCEANCE_QUERY)
                .itemPreparedStatementSetter(new SeanceItemPreparedStatementSetter())
                .build();
    }


    @Bean
    public StepExecutionListener chargementSeanceStepListener() {
        return new ChargementSeanceStepListener();
    }


    @Bean
    public ConversionService stringToLocalDateConvertionService(Converter stringToLocalDateConverter) {
        DefaultConversionService dcs = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(dcs);
        dcs.addConverter(stringToLocalDateConverter);
        return dcs;
    }


    @Bean
    public Converter stringToLocalDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String input) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("ddMMyyyy");
                return LocalDate.parse(input, df);
            }
        };
    }


    @Bean
    public FieldSetMapper<Seance> seanceFieldSetMapper(final ConversionService stringToLocalDateConvertionService) {
        BeanWrapperFieldSetMapper<Seance> bean = new BeanWrapperFieldSetMapper<>();
        bean.setTargetType(Seance.class);
        bean.setConversionService(stringToLocalDateConvertionService);
        return bean;
    }

    @Bean
    public SkipPolicy seanceSkipPolicy() {
        return new SeanceSkipPolicy();
    }


    /**
     * TXT
     *
     * @param stepBuilderFactory
     * @return
     */
    @Bean
    public Step chargementSeanceTxtStep(final StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("chargementFormationStep")
                .<Seance, Seance>chunk(2)
                .reader(seanceTxtItemReader(null))
                .writer(sceanceItemWriter(null))
                .faultTolerant()
                .skipPolicy(seanceSkipPolicy())
                .listener(chargementSeanceStepListener())
                .build();
    }


    /**
     * CSV
     *
     * @param stepBuilderFactory
     * @return
     */
    @Bean
    public Step chargementSeanceCsvStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("chargementFormateurStep")
                .<Seance, Seance>chunk(2)
                .reader(seanceCsvItemReader(null))
                .writer(sceanceItemWriter(null))
                .faultTolerant()
                .skipPolicy(seanceSkipPolicy())
                .listener(chargementSeanceStepListener())
                .build();
    }
}
