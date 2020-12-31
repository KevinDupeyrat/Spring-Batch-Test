package com.test.batch.step;

import com.test.batch.BasicTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;


public class ChargementFormationStepConfigTest extends BasicTest {

    @Test
    public void should_load_formation_with_success() {
        JobParameters jobParameters = new JobParametersBuilder()
                    .addString("formationsFile", "classpath:/inputs/formationsFile.xml")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchStep("chargementFormationStep", jobParameters);

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Integer queryResult = jdbcTemplate.queryForObject("select count(*) from formations", Integer.class);

        Assert.assertEquals((Integer) 3, queryResult);
    }

}