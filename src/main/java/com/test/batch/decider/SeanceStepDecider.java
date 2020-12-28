package com.test.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.util.StringUtils;

public class SeanceStepDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (StringUtils.endsWithIgnoreCase(stepExecution.getJobParameters().getString("seancesFile"),
                "txt")) {
            return new FlowExecutionStatus("txt");
        }
        return new FlowExecutionStatus("csv");
    }
}
