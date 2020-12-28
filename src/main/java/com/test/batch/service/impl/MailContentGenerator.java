package com.test.batch.service.impl;

import com.test.batch.model.Planning;
import com.test.batch.service.contract.IMailContentGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;

public class MailContentGenerator implements IMailContentGenerator {

    private final Template template;

    public MailContentGenerator(final Configuration configuration) throws IOException {
        super();
        template = configuration.getTemplate("planning.ftl");
    }

    @Override
    public String generate(final Planning planning) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        template.process(planning, stringWriter);
        return stringWriter.toString();
    }
}
