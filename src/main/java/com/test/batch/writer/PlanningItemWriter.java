package com.test.batch.writer;

import com.test.batch.model.Planning;
import com.test.batch.service.contract.IMailContentGenerator;
import com.test.batch.service.contract.IPlanningMailSenderService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class PlanningItemWriter implements ItemWriter<Planning> {

    private final IMailContentGenerator mailContentGenerator;

    private final IPlanningMailSenderService planningMailSenderService;

    @Override
    public void write(List<? extends Planning> list) throws Exception {
        list.forEach(planning -> {
            try {
                String content = mailContentGenerator.generate(planning);
                planningMailSenderService.send(planning.getFormateur().getAdresseEmail(), content);
            } catch (IOException | TemplateException | MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
