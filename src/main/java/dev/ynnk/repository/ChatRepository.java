package dev.ynnk.repository;

import dev.ynnk.model.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long>{

    public Iterable<Chat> findAllByPersonAUsernameOrPersonBUsername(String usernameA, String usernameB);

}
