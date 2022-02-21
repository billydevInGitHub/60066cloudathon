package com.billydev.exp053;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageSendController {

    private final Logger logger = LoggerFactory.getLogger(MessageSendController.class);
    private static final String DESTINATION_NAME = "salesperformanemessages";

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping(path = "/messages")
    public String postMessage(@RequestParam String message) {
        logger.info("Sending message");
        jmsTemplate.convertAndSend(DESTINATION_NAME, new User(message));
        return message+" sent";
    }
}
