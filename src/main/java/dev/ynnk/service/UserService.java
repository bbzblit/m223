package dev.ynnk.service;

import dev.ynnk.model.User;
import dev.ynnk.repository.UserRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements ParentService<User, String>, UserDetailsService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(User entity) {
        return this.userRepository.save(entity);
    }

    @Override
    public User findById(String s) {
        return this.userRepository.findById(s)
                .orElse(null);
    }

    @Override
    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public void deleteById(String s) {
        this.userRepository.deleteById(s);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }

    public User getCurrentLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.findById(authentication.getName());
    }

}
