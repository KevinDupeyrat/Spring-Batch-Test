package com.test.batch.step;

import com.test.batch.BasicTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:/init-formations-formateurs-tables.sql")
public class ChargementSeanceStepConfigTest extends BasicTest {

    @Test
    public void should_load_seance_csv_with_success() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("seancesFile", "classpath:/inputs/seancesFile.csv")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchStep("chargementSeanceCsvStep", jobParameters);

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Integer queryResult = jdbcTemplate.queryForObject("select count(*) from seances", Integer.class);

        Assert.assertEquals((Integer) 2, queryResult);
    }

    @Test
    public void should_load_seance_txt_with_success() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("seancesFile", "classpath:/inputs/seancesFile.txt")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchStep("chargementSeanceTxtStep", jobParameters);

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Integer queryResult = jdbcTemplate.queryForObject("select count(*) from seances", Integer.class);

        Assert.assertEquals((Integer) 3, queryResult);
    }

}