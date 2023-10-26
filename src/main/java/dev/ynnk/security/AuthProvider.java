package dev.ynnk.security;

import dev.ynnk.service.HashService;
import dev.ynnk.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationProvider;

@Component
public class AuthProvider implements AuthenticationProvider{

    private final UserService userService;

    private final HashService securityService;

    public AuthProvider(final UserService userService, final HashService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = this.userService.loadUserByUsername(username);


        if (!this.securityService.check(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
