package dev.ynnk.service;

import dev.ynnk.model.Chat;
import dev.ynnk.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService implements ParentService<Chat, Long> {

    private final ChatRepository chatRepository;


    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }


    @Override
    public Chat save(Chat entity) {
        return this.chatRepository.save(entity);
    }

    @Override
    public Chat findById(Long aLong) {
        return this.chatRepository.findById(aLong)
                .orElse(null);
    }

    @Override
    public Iterable<Chat> findAll() {
        return this.chatRepository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {
        this.chatRepository.deleteById(aLong);
    }

    public Iterable<Chat> findAllByUser(String username){
        return this.chatRepository.findAllByPersonAUsernameOrPersonBUsername(username, username);
    }
}
