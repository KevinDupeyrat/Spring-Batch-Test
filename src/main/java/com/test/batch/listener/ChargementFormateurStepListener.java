package com.test.batch.listener;

import com.test.batch.model.Formateur;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepListenerSupport;

@Slf4j
public class ChargementFormateurStepListener extends StepListenerSupport<Formateur, Formateur>
        implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Chargement des Formateurs :{} formateur(s) enregistré(s)", stepExecution.getReadCount());
        return stepExecution.getExitStatus();
    }
}
