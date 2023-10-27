package dev.ynnk;

import dev.ynnk.model.EmailChange;
import dev.ynnk.model.User;
import dev.ynnk.repository.EmailChangeRepository;
import dev.ynnk.service.EmailChangeService;
import dev.ynnk.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class EmailChangeServiceTest {

    @Mock
    private EmailChangeRepository emailChangeRepository;

    @Mock
    private MailService mailService;

    private EmailChangeService emailChangeService;

    @Value("${dev.ynnk.hostnames}")
    private String hostnames;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailChangeService = new EmailChangeService(emailChangeRepository, mailService);
    }

    @Test
    public void testSave() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        EmailChange emailChange = new EmailChange();
        emailChange.setUser(user);
        emailChange.setId("123");

        Optional<EmailChange> emailChangeOptional = Optional.of(new EmailChange());
        when(emailChangeRepository.findByUserUsername(user.getUsername())).thenReturn(emailChangeOptional);

        // Act
        emailChangeService.save(emailChange);

        // Assert
        verify(emailChangeRepository, times(1)).deleteById(emailChangeOptional.get().getId());
        verify(mailService, times(1)).sendMail(eq(user.getEmail()), eq("Email change"),
                eq("Please click on the following link to change your email: " +
                        hostnames  + "/email-change/confirm?id=" + emailChange.getId()));
        verify(emailChangeRepository, times(1)).save(emailChange);
    }

    @Test
    public void testFindById() {
        // Arrange
        String id = "123";
        EmailChange emailChange = new EmailChange();
        when(emailChangeRepository.findById(id)).thenReturn(Optional.of(emailChange));

        // Act
        EmailChange result = emailChangeService.findById(id);

        // Assert
        verify(emailChangeRepository, times(1)).findById(id);
        assert(result == emailChange);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Iterable<EmailChange> emailChanges = mock(Iterable.class);
        when(emailChangeRepository.findAll()).thenReturn(emailChanges);

        // Act
        Iterable<EmailChange> result = emailChangeService.findAll();

        // Assert
        verify(emailChangeRepository, times(1)).findAll();
        assert(result == emailChanges);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        String id = "123";

        // Act
        emailChangeService.deleteById(id);

        // Assert
        verify(emailChangeRepository, times(1)).deleteById(id);
    }
}