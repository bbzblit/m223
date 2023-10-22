package dev.ynnk.service;

import dev.ynnk.model.Message;
import dev.ynnk.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService implements ParentService<Message, Long>{

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message entity) {
        return this.messageRepository.save(entity);
    }

    @Override
    public Message findById(Long id) {
        return this.messageRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Iterable<Message> findAll() {
        return this.messageRepository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {
        this.messageRepository.deleteById(aLong);
    }

    public Iterable<Message> findAllSinceOfChat(Instant since, String chatId){
        return this.messageRepository.findAllByCreationDateGreaterThanEqualAndChatId(since, Long.parseLong(chatId));
    }
}
