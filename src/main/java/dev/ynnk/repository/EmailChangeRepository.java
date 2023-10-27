package dev.ynnk.repository;

import dev.ynnk.model.EmailChange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailChangeRepository extends CrudRepository<EmailChange, String> {

    Optional<EmailChange> findByUserUsername(String username);
}
