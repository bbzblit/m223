package dev.ynnk;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinServletRequest;
import dev.ynnk.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static org.mockito.Mockito.*;

public class SecurityServiceTest {

    @Mock
    private UI ui;

    @Mock
    private VaadinServletRequest vaadinServletRequest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private SecurityContextLogoutHandler logoutHandler;

    @Mock
    private Page page;


    private SecurityService securityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        securityService = new SecurityService(logoutHandler);
    }

    @Test
    public void testLogout() {
        // Arrange
        try (MockedStatic<UI> ui = mockStatic(UI.class)) {
            ui.when(UI::getCurrent).thenReturn(this.ui);
            when(this.ui.getPage()).thenReturn(this.page);
            try (MockedStatic<VaadinServletRequest> vaadinServletRequest = mockStatic(VaadinServletRequest.class)) {
                vaadinServletRequest.when(VaadinServletRequest::getCurrent).thenReturn(this.vaadinServletRequest);
                when(this.vaadinServletRequest.getHttpServletRequest()).thenReturn(this.httpServletRequest);

                // Act
                securityService.logout();

                // Assert
                verify(this.ui.getPage(), times(1)).setLocation("/login");
                verify(this.logoutHandler, times(1)).logout(this.httpServletRequest, null, null);
            }
        }
    }
}