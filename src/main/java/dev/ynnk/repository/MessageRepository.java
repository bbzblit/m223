package dev.ynnk.repository;

import dev.ynnk.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    public Iterable<Message> findAllByCreationDateGreaterThanEqualAndChatId(Instant time, Long id);
}
