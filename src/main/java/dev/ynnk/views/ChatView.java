package dev.ynnk.views;

import com.vaadin.collaborationengine.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;
import dev.ynnk.manager.MessageCallback;
import dev.ynnk.model.Chat;
import dev.ynnk.model.Message;
import dev.ynnk.model.User;
import dev.ynnk.service.MessageService;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.stream.Streams;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@PageTitle("Chat")
@Route(value = "")
@RouteAlias(value = "")
@PermitAll
public class ChatView extends VerticalLayout {

    private final MessageService messageService;
    private final UserService userService;


    private UserInfo currentUser;


    private CollaborationMessageList messages;

    private CollaborationMessageInput chatInput;


    public ChatView(final MessageService messageService, final MessageCallback callback, final UserService userService) {

        this.userService = userService;
        this.messageService = messageService;

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setAccountNonLocked(true);
        this.userService.save(user);



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        user = this.userService.findById(authentication.getName());


        this.currentUser  = new UserInfo(user.getUsername(), user.getUsername());

        this.messages = new CollaborationMessageList(currentUser, "general", callback);
        this.chatInput = new CollaborationMessageInput(messages);

        VerticalLayout chatWrapper = new VerticalLayout();
        chatWrapper.add(this.messages, this.chatInput);



        this.messages.setWidthFull();
        this.chatInput.setWidthFull();
        chatWrapper.setWidthFull();

        setMargin(true);
        setPadding(true);

        add(chatWrapper);
    }

}
