package com.test.batch.step;

import com.test.batch.BasicTest;
import com.test.batch.service.impl.PlanningMailSenderService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.context.jdbc.Sql;

import javax.mail.MessagingException;

public class PlanningStepConfigTest extends BasicTest {

    @Test
    @Sql(scripts = "classpath:/init-all-tables.sql")
    public void should_send_email() throws MessagingException {

        JobExecution result = jobLauncherTestUtils.launchStep("planningStep");

        Assert.assertEquals(ExitStatus.COMPLETED, result.getExitStatus());

        Mockito.verify(planningMailSenderService, Mockito.times(4))
                .send(Mockito.any(), Mockito.any());
    }

}
