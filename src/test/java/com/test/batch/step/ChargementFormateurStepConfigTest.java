package com.test.batch.step;

import com.test.batch.BasicTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;


public class ChargementFormateurStepConfigTest extends BasicTest {

    @Test
    public void should_load_formateur_with_success() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:/inputs/formateursFile.csv")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchStep("chargementFormateurStep", jobParameters);

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Integer queryResult = jdbcTemplate.queryForObject("select count(*) from formateurs", Integer.class);

        Assert.assertEquals((Integer) 13, queryResult);
    }

}