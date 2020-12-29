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
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

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
    public Flow chargementFormateurFlow(final Step chargementFormateurStep) {
        return new FlowBuilder<Flow>("chargementFormateurStep")
                .start(chargementFormateurStep)
                .end();
    }


    @Bean
    public Flow chargementFormationFlow(final Step chargementFormationStep) {
        return new FlowBuilder<Flow>("chargementFormationFlow")
                .start(chargementFormationStep)
                .end();
    }


    @Bean
    public Flow parallelChargementFormateurAndFormationFlow() {
        return new FlowBuilder<Flow>("parallelChargementFormateurAndFormationFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(chargementFormationFlow(null), chargementFormateurFlow(null))
                .end();
    }


    @Bean
    public Job formateurs(final JobBuilderFactory jobBuilderFactory,
                          final Step chargementSeanceTxtStep,
                          final Step chargementSeanceCsvStep,
                          final Step planningStep) {
        return jobBuilderFactory.get("formateurs")
                .start(parallelChargementFormateurAndFormationFlow())
                .next(seanceDecider()).on("txt").to(chargementSeanceTxtStep)
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
