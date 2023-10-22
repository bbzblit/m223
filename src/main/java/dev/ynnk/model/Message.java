package dev.ynnk.model;


import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Chat chat;


    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    private String message;


    private Instant creationDate;

    public static CollaborationMessage toCollaborationMessage(Message message){
        User author = message.getAuthor();
        UserInfo userInfo = new UserInfo(author.getUsername(), author.getUsername());

        return new CollaborationMessage(
                userInfo,
                message.getMessage(),
                message.getCreationDate()
        );
    }

    public static Message fromCollaborationMessage(CollaborationMessage message, String topic){
        return Message.builder()
                .author(User.builder().username(message.getUser().getName()).build())
                .message(message.getText())
                .chat(Chat.builder().id(Long.parseLong(topic)).build())
                .creationDate(message.getTime())
                .build();
    }

}
