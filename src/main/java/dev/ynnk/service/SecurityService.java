package dev.ynnk.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public final class SecurityService {

    @Value("${dev.ynnk.security.pepper}")
    private transient String pepper;

    public String hash(String password){
        return BCrypt.hashpw(password + pepper, BCrypt.gensalt());
    }

    public boolean check(String password, String hash){
        return BCrypt.checkpw(password + pepper, hash);
    }


    public void setPepper(final String pepper) {
        this.pepper = pepper;
    }
}
