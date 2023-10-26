package dev.ynnk.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final SecurityContextLogoutHandler logoutHandler;

    public SecurityService(SecurityContextLogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
    }

    public SecurityService() {
        this(new SecurityContextLogoutHandler());
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/login");
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

}
