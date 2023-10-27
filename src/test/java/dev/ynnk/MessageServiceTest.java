package dev.ynnk;

import dev.ynnk.model.Message;
import dev.ynnk.repository.MessageRepository;
import dev.ynnk.service.MessageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService = new MessageService(messageRepository);
    }

    @Test
    public void testSave() {
        // Arrange
        Message message = new Message();
        message.setId(1L);
        message.setMessage("Hello, world!");

        when(messageRepository.save(message)).thenReturn(message);

        // Act
        Message savedMessage = messageService.save(message);

        // Assert
        assertEquals(message, savedMessage);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void testFindById() {
        // Arrange
        Message message = new Message();
        message.setId(1L);
        message.setMessage("Hello, world!");

        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        // Act
        Message foundMessage = messageService.findById(1L);

        // Assert
        assertEquals(message, foundMessage);
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Message> messages = new ArrayList<>();
        Message message1 = new Message();
        message1.setId(1L);
        message1.setMessage("Hello, world!");
        messages.add(message1);
        Message message2 = new Message();
        message2.setId(2L);
        message2.setMessage("How are you?");
        messages.add(message2);

        when(messageRepository.findAll()).thenReturn(messages);

        // Act
        Iterable<Message> foundMessages = messageService.findAll();

        // Assert
        assertEquals(messages, foundMessages);
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        messageService.deleteById(id);

        // Assert
        verify(messageRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindAllSinceOfChat() {
        // Arrange
        Instant since = Instant.now();
        String chatId = "1";
        List<Message> messages = new ArrayList<>();
        Message message1 = new Message();
        message1.setId(1L);
        message1.setMessage("Hello, world!");
        messages.add(message1);
        Message message2 = new Message();
        message2.setId(2L);
        message2.setMessage("How are you?");
        messages.add(message2);

        when(messageRepository.findAllByCreationDateGreaterThanEqualAndChatId(since, Long.parseLong(chatId))).thenReturn(messages);

        // Act
        Iterable<Message> foundMessages = messageService.findAllSinceOfChat(since, chatId);

        // Assert
        assertEquals(messages, foundMessages);
        verify(messageRepository, times(1)).findAllByCreationDateGreaterThanEqualAndChatId(since, Long.parseLong(chatId));
    }
}