package com.test.batch.listener;

import com.test.batch.model.Formation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepListenerSupport;

@Slf4j
public class ChargementFormationStepListener extends StepListenerSupport<Formation, Formation>
        implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Chargement des Formations :{} formation(s) enregistrée(s)", stepExecution.getReadCount());
        return stepExecution.getExitStatus();
    }

}
