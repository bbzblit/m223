package dev.ynnk;

import dev.ynnk.model.User;
import dev.ynnk.repository.UserRepository;
import dev.ynnk.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);

        this.testUser = User.builder()
                .username("testuser")
                .password("testpassword")
                .build();

    }

    @Test
    public void testSaveUser() {
        //Arrange
        when(userRepository.save(this.testUser)).thenReturn(this.testUser);

        //Act
        User savedUser = userService.save(this.testUser);

        //Assert
        assertNotNull(savedUser);
        assertEquals(this.testUser.getUsername(), savedUser.getUsername());
        assertEquals(this.testUser.getPassword(), savedUser.getPassword());

        verify(userRepository, times(1)).save(this.testUser);
    }

    @Test
    public void testFindUserById() {
        //Arrange
        when(userRepository.findById("testuser")).thenReturn(Optional.of(this.testUser));

        //Act
        User foundUser = userService.findById("testuser");

        //Assert
        assertNotNull(foundUser);
        assertEquals(this.testUser.getUsername(), foundUser.getUsername());
        assertEquals(this.testUser.getPassword(), foundUser.getPassword());

        verify(userRepository, times(1)).findById("testuser");
    }

    @Test
    public void testFindAllUsers() {

        // Arrange
        when(userRepository.findAll()).thenReturn(null);

        // Act
        Iterable<User> users = userService.findAll();

        // Assert
        assertNull(users);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteById("myuser");

        verify(userRepository, times(1)).deleteById("myuser");
    }

    @Test
    public void testLoadUserByUsername() {

        // Arrange
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(this.testUser));

        // Act
        User foundUser = (User) userService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals(this.testUser.getUsername(), foundUser.getUsername());
        assertEquals(this.testUser.getPassword(), foundUser.getPassword());

        verify(userRepository, times(1)).findUserByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsernameThrowsException() {

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(this.testUser));

        when(userRepository.findUserByUsername("anotherUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("anotherUser"));

        verify(userRepository, times(1)).findUserByUsername("anotherUser");
    }

}