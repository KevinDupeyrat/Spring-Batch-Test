package com.test.batch.service.contract;

import com.test.batch.model.Planning;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface IMailContentGenerator {

    String generate(Planning planning) throws IOException, TemplateException;
}
