package dev.ynnk;

import dev.ynnk.service.HashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class HashServiceTest {

    @Autowired
    private HashService hashService;

    private String testPassword;

    @BeforeEach
    public void init() {
        this.testPassword = "testpassword";
    }

    @Test
    public void testHash() {
        // Act
        String hashedPassword = hashService.hash(testPassword);

        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(testPassword, hashedPassword);
    }

    @Test
    public void testCheck() {
        // Arrange
        String hashedPassword = hashService.hash(testPassword);

        // Act
        boolean result = hashService.check(testPassword, hashedPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckWithWrongPassword() {
        // Arrange
        String hashedPassword = hashService.hash(testPassword);

        // Act
        boolean result = hashService.check("wrongpassword", hashedPassword);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIfSaltChanges() {
        // Arrange
        String hashedPassword = hashService.hash(testPassword);
        String hashedPassword2 = hashService.hash(testPassword);

        // Act
        boolean result = hashService.check(testPassword, hashedPassword);
        boolean result2 = hashService.check(testPassword, hashedPassword2);

        // Assert
        assertTrue(result);
        assertTrue(result2);
        assertNotEquals(hashedPassword, hashedPassword2);
    }

}