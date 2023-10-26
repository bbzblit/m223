package dev.ynnk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService{

    @Autowired
    private JavaMailSender emailSender;


    @Value("${dev.ynnk.email.enabled}")
    private boolean enabled;

    @Value("${dev.ynnk.mail.address}")
    private String mailAddress;

    private final Logger LOG = LoggerFactory.getLogger(MailService.class);

    public void sendMail(String to, String subject, String text){
        if (enabled){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(mailAddress);

            emailSender.send(message);
        } else{
            LOG.info("Mail sending is disabled");
            LOG.info("Mail details:");
            LOG.info("To: " + to);
            LOG.info("Subject: " + subject);
            LOG.info("Text: " + text);
        }
}

}
