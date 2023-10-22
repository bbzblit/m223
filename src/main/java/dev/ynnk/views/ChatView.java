package dev.ynnk.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.ynnk.component.ChatComponent;
import dev.ynnk.manager.MessageCallback;
import dev.ynnk.model.Chat;
import dev.ynnk.model.User;
import dev.ynnk.service.ChatService;
import dev.ynnk.service.MessageService;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PageTitle("Chat")
@Route(value = "")
@RouteAlias(value = "")
@PermitAll
public class ChatView extends HorizontalLayout {


    private ChatComponent chatComponent;

    private final MessageService messageService;

    private final UserService userService;

    private final ChatService chatService;

    private final MessageCallback messageCallback;

    private final User currentUser;

    private final Aside aside = new Aside();

    private final Tabs tabs = new Tabs();

    private Div chatWrapper = new Div();

    private void loadChats(){
        for (Chat chat : this.chatService.findAllByUser(this.currentUser.getUsername())) {
            String username = chat.getPersonA().getUsername().equals(this.currentUser.getUsername()) ?
                    chat.getPersonB().getUsername() :
                    chat.getPersonA().getUsername();

            this.tabs.add(new Tab(username));
        }

        this.aside.add(this.tabs);
    }

    private void initChatPlaceholder(){
        VerticalLayout chatPlaceholder = new VerticalLayout();

        chatPlaceholder.removeAll();
        chatPlaceholder.addClassName("chat-placeholder");
        chatPlaceholder.getStyle().set("display", "flex");
        chatPlaceholder.getStyle().set("justify-content", "center");
        chatPlaceholder.getStyle().set("align-items", "center");


        H1 h1 = new H1();
        h1.setText("Welcome back " + this.currentUser.getUsername());

        H3 h2 = new H3();
        h2.setText("Select a chat to start messaging or create a new one");

        chatPlaceholder.add(h1, h2);
        chatPlaceholder.setWidthFull();
        chatPlaceholder.setHeightFull();

        this.chatWrapper.add(chatPlaceholder);
    }

    public ChatView(final MessageService messageService,
                    final UserService userService,
                    final MessageCallback messageCallback,
                    final ChatService chatService){

        this.messageService = messageService;
        this.userService = userService;
        this.messageCallback = messageCallback;
        this.chatService = chatService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.currentUser = this.userService.findById(authentication.getName());

        EmailField emailField = new EmailField();
        emailField.setLabel("Create new chat");
        emailField.setClearButtonVisible(true);

        loadChats();

        this.aside.add(emailField, this.tabs);

        this.aside.getStyle().set("margin-left", "auto");

        initChatPlaceholder();

        this.chatWrapper.setWidthFull();
        this.chatWrapper.setHeightFull();

        setMargin(true);

        add(chatWrapper, this.aside);

    }



}
