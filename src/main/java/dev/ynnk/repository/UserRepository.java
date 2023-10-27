package dev.ynnk.repository;

import dev.ynnk.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findUserByUsername(String username);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends User> S save(S user);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(String s);


}
