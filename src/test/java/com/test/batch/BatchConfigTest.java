package com.test.batch;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BatchConfigTest {

    @Rule
    public GreenMailRule serverSmtp = new GreenMailRule(
            new ServerSetup(2525, "localhost", ServerSetup.PROTOCOL_SMTP)
    );

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Test
    public void should_execute_job_with_success() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:/inputs/formateursFile.csv")
                .addString("formationsFile", "classpath:/inputs/formationsFile.xml")
                .addString("seancesFile", "classpath:/inputs/seancesFile.txt")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchJob(jobParameters);

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Integer formateurResult = jdbcTemplate.queryForObject("select count(*) from formateurs", Integer.class);
        Integer formationResult = jdbcTemplate.queryForObject("select count(*) from formations", Integer.class);
        Integer seanceResult = jdbcTemplate.queryForObject("select count(*) from seances", Integer.class);


        Assert.assertEquals((Integer) 13, formateurResult);
        Assert.assertEquals((Integer) 3, formationResult);
        Assert.assertEquals((Integer) 3, seanceResult);

        Assert.assertEquals(3, serverSmtp.getReceivedMessages().length);
        Assert.assertEquals("Votre planning des formations", serverSmtp.getReceivedMessages()[0].getSubject());

    }

}