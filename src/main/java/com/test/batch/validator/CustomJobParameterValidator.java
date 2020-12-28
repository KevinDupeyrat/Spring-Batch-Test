package com.test.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class CustomJobParameterValidator implements JobParametersValidator{

    @Override
    public void validate(final JobParameters jobParameters) throws JobParametersInvalidException {

        if (jobParameters == null) {
            return;
        }

        if (!StringUtils.endsWithIgnoreCase(jobParameters.getString("formateursFile"), "csv")) {
            throw new  JobParametersInvalidException("Le fichier des formateurs doit être en CSV");
        }

        if (!StringUtils.endsWithIgnoreCase(jobParameters.getString("formationsFile"), "xml")) {
            throw new  JobParametersInvalidException("Le fichier des formations doit être en XML");
        }

        if (!StringUtils.endsWithIgnoreCase(jobParameters.getString("seancesFile"), "txt") &&
                !StringUtils.endsWithIgnoreCase(jobParameters.getString("seancesFile"), "csv")) {
            throw new  JobParametersInvalidException("Le fichier des séance doit être en CSV");
        }
    }
}
