package dev.ynnk.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "login_user")
public class User implements UserDetails {

    @Id
    private String username;

    private String password;

    private String email;

    private boolean is_admin;

    private boolean accountNonLocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (this.is_admin) {
            authorities.add((GrantedAuthority) () -> "ROLE_ADMIN");
        }
        authorities.add((GrantedAuthority) () -> "ROLE_USER");
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return true;
    }



}