package dev.ynnk;

import dev.ynnk.model.Chat;
import dev.ynnk.repository.ChatRepository;
import dev.ynnk.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatService(chatRepository);
    }

    @Test
    public void testSave() {
        // Arrange
        Chat chat = new Chat();
        chat.setId(1L);
        when(chatRepository.save(chat)).thenReturn(chat);

        // Act
        Chat result = chatService.save(chat);

        // Assert
        verify(chatRepository, times(1)).save(chat);
        assert(result == chat);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        Chat chat = new Chat();
        when(chatRepository.findById(id)).thenReturn(Optional.of(chat));

        // Act
        Chat result = chatService.findById(id);

        // Assert
        verify(chatRepository, times(1)).findById(id);
        assert(result == chat);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Chat> chats = new ArrayList<>();
        when(chatRepository.findAll()).thenReturn(chats);

        // Act
        Iterable<Chat> result = chatService.findAll();

        // Assert
        verify(chatRepository, times(1)).findAll();
        assert(result == chats);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        chatService.deleteById(id);

        // Assert
        verify(chatRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindAllByUser() {
        // Arrange
        String username = "testuser";
        List<Chat> chats = new ArrayList<>();
        when(chatRepository.findAllByPersonAUsernameOrPersonBUsername(username, username)).thenReturn(chats);

        // Act
        Iterable<Chat> result = chatService.findAllByUser(username);

        // Assert
        verify(chatRepository, times(1)).findAllByPersonAUsernameOrPersonBUsername(username, username);
        assert(result == chats);
    }

    @Test
    public void testFindByUsernames() {
        // Arrange
        String usernameA = "testuser1";
        String usernameB = "testuser2";
        Chat chat = new Chat();
        when(chatRepository.findByPersonAUsernameAndPersonBUsername(usernameA, usernameB)).thenReturn(Optional.of(chat));

        // Act
        Chat result = chatService.findByUsernames(usernameA, usernameB);

        // Assert
        verify(chatRepository, times(1)).findByPersonAUsernameAndPersonBUsername(usernameA, usernameB);
        assert(result == chat);
    }
}