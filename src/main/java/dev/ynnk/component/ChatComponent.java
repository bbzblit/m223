package dev.ynnk.component;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import dev.ynnk.manager.MessageCallback;
import dev.ynnk.model.User;
import dev.ynnk.service.MessageService;
import dev.ynnk.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ChatComponent extends VerticalLayout {


    private final UserService userService;


    private final UserInfo currentUser;


    private final CollaborationMessageList messages;

    private final CollaborationMessageInput chatInput;


    public ChatComponent(
            final MessageCallback callback,
            final UserService userService,
            final String chatName) {

        this.userService = userService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findById(authentication.getName());


        this.currentUser = new UserInfo(user.getUsername(), user.getUsername());

        this.messages = new CollaborationMessageList(currentUser, chatName, callback);
        this.chatInput = new CollaborationMessageInput(messages);

        VerticalLayout chatWrapper = new VerticalLayout();
        this.messages.setHeightFull();

        chatWrapper.add(this.messages, this.chatInput);


        this.messages.setWidthFull();
        this.chatInput.setWidthFull();
        chatWrapper.setWidthFull();
        chatWrapper.setHeightFull();

        add(chatWrapper);
        setHeightFull();
    }

}
