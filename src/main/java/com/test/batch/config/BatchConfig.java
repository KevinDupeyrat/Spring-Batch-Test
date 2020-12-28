package com.test.batch.config;

import com.test.batch.decider.SeanceStepDecider;
import com.test.batch.validator.CustomJobParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {


    @Bean
    public JobParametersValidator jobParametersValidator() {
        DefaultJobParametersValidator bean = new DefaultJobParametersValidator();
        bean.setRequiredKeys(new String[]{"formateursFile", "formationsFile", "seancesFile"});
        bean.setOptionalKeys(new String[]{"run.id"});
        return bean;
    }


    @Bean
    public JobParametersValidator compositeJobParametersValidator() {
        CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();
        compositeJobParametersValidator.setValidators(List.of(jobParametersValidator(),
                new CustomJobParameterValidator()));
        return compositeJobParametersValidator;
    }


    @Bean
    public JobExecutionDecider seanceDecider() {
        return new SeanceStepDecider();
    }


    @Bean
    public Job formateurs(final JobBuilderFactory jobBuilderFactory,
                          final Step chargementFormateurStep,
                          final Step chargementFormationStep,
                          final Step chargementSeanceTxtStep,
                          final Step chargementSeanceCsvStep,
                          final Step planningStep) {
        return jobBuilderFactory.get("formateurs")
                .start(chargementFormateurStep)
                .next(chargementFormationStep)
                .next(seanceDecider())
                .from(seanceDecider()).on("txt").to(chargementSeanceTxtStep)
                .from(seanceDecider()).on("csv").to(chargementSeanceCsvStep)
                .from(chargementSeanceTxtStep).on("*").to(planningStep)
                .from(chargementSeanceCsvStep).on("*").to(planningStep)
                .from(planningStep).on("*").end()
                .end()
                .validator(compositeJobParametersValidator())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
