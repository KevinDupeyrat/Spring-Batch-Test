package com.test.batch.service.impl;

import com.test.batch.service.contract.IPlanningMailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
public class PlanningMailSenderService implements IPlanningMailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String destination, String content) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        message.setContent(content, "text/html");
        messageHelper.setTo(destination);
        messageHelper.setSubject("Votre planning des formations");

        javaMailSender.send(message);
    }
}
