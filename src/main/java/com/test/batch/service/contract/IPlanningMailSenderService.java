package com.test.batch.service.contract;

import javax.mail.MessagingException;

public interface IPlanningMailSenderService {

    void send(String destination, String content) throws MessagingException;
}
