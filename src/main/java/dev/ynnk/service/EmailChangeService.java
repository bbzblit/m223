package dev.ynnk.service;

import com.sun.jna.platform.win32.WinNT;
import dev.ynnk.model.EmailChange;
import dev.ynnk.repository.EmailChangeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailChangeService implements ParentService<EmailChange, String> {

    @Value("${dev.ynnk.hostnames}")
    private String hostnames;

    private final EmailChangeRepository emailChangeRepository;

    private final MailService mailService;

    public EmailChangeService(EmailChangeRepository emailChangeRepository, final MailService mailService) {
        this.emailChangeRepository = emailChangeRepository;
        this.mailService = mailService;
    }

    @Override
    public EmailChange save(EmailChange emailChange) {

        Optional<EmailChange> emailChangeOptional =
                emailChangeRepository.findByUserUsername(emailChange.getUser().getUsername());

        emailChangeOptional.ifPresent(change -> emailChangeRepository.deleteById(change.getId()));

        mailService.sendMail(emailChange.getUser().getEmail(), "Email change",
                "Please click on the following link to change your email: " +
                        hostnames  + "/email-change/confirm?id=" + emailChange.getId() );

        return emailChangeRepository.save(emailChange);
    }

    @Override
    public EmailChange findById(String id) {
        return emailChangeRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<EmailChange> findAll() {
        return emailChangeRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        emailChangeRepository.deleteById(id);
    }
}
