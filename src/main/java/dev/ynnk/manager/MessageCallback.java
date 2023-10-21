package dev.ynnk.manager;

import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;
import com.vaadin.flow.spring.annotation.SpringComponent;
import dev.ynnk.model.Message;
import dev.ynnk.service.MessageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringComponent
public class MessageCallback implements CollaborationMessagePersister {

    private final MessageService messageService;

    public MessageCallback(final MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<CollaborationMessage> fetchMessages(FetchQuery fetchQuery) {
        return StreamSupport.stream(
                messageService.findAllSinceOfChat(fetchQuery.getSince(),
                        fetchQuery.getTopicId()).spliterator(), false)
                .map(Message::toCollaborationMessage);
    }

    @Override
    public void persistMessage(PersistRequest persistRequest) {
        this.messageService.save(Message.fromCollaborationMessage(
                persistRequest.getMessage(), persistRequest.getTopicId()));
    }
}
