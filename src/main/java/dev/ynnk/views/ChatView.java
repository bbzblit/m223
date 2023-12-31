package dev.ynnk.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.ynnk.MainLayout;
import dev.ynnk.component.ChatComponent;
import dev.ynnk.manager.MessageCallback;
import dev.ynnk.model.Chat;
import dev.ynnk.model.User;
import dev.ynnk.service.ChatService;
import dev.ynnk.service.MessageService;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Chat")
@Route(value = "", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class ChatView extends HorizontalLayout {


    private ChatComponent chatComponent;

    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;
    private final MessageCallback messageCallback;
    private final User currentUser;
    private final Aside aside = new Aside();
    private final Tabs tabs = new Tabs();
    private final List<String> tabUsernames = new ArrayList<>();
    private Div chatWrapper = new Div();

    private void loadChats(){
        this.tabs.setOrientation(Tabs.Orientation.VERTICAL);

        for (Chat chat : this.chatService.findAllByUser(this.currentUser.getUsername())) {
            String username = chat.getPersonA().getUsername().equals(this.currentUser.getUsername()) ?
                    chat.getPersonB().getUsername() :
                    chat.getPersonA().getUsername();

            this.tabUsernames.add(username);
            Tab tab = new Tab(username);
            tab.getStyle().set("cursor", "pointer");
            this.tabs.add(tab);
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

    private void initStartChatArea(){
        TextField usernameField = new TextField();

        usernameField.setLabel("Username");
        usernameField.setClearButtonVisible(true);

        Button startChatButton = new Button("Start Chat");
        startChatButton.getStyle().set("cursor", "pointer");
        startChatButton.addClickListener(
                clickEvent -> {


                    User user = this.userService.findById(usernameField.getValue());

                    if (user == null){
                        usernameField.setErrorMessage("User not found");
                        usernameField.setInvalid(true);
                        return;
                    }

                    usernameField.clear();

                    int index = this.tabUsernames.indexOf(user.getUsername());
                    if(index != -1){
                        this.tabs.setSelectedIndex(index);
                        return;
                    }

                    Chat chat = new Chat();
                    chat.setPersonA(this.currentUser);
                    chat.setPersonB(user);
                    this.chatService.save(chat);

                    this.tabUsernames.add(user.getUsername());
                    this.tabs.add(new Tab(user.getUsername()));

                    Tab tab = this.tabs.getSelectedTab();
                    this.changeChat(tab.getLabel());
                }
        );

        Div devider = new Div();
        devider.getStyle().set("height", "1px").set("background-color", "#e0e0e0").set("margin", "1rem 0");

        this.aside.removeAll();
        this.aside.add(usernameField, startChatButton, devider);
    }

    private void changeChat(String username){
        User user = this.userService.findById(username);
        Chat chat = this.chatService.findByUsernames(this.currentUser.getUsername(), user.getUsername());
        ChatComponent chatComponent = new ChatComponent(this.messageCallback, this.userService, chat.getId().toString());

        this.chatWrapper.removeAll();
        this.chatWrapper.add(chatComponent);
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

        H1 asideTitle = new H1();
        asideTitle.setText("Your Chats");

        this.aside.add(asideTitle);


        loadChats();

        initStartChatArea();


        this.aside.add(this.tabs);

        this.aside.getStyle().set("margin-left", "auto");

        initChatPlaceholder();

        this.chatWrapper.setWidthFull();
        this.chatWrapper.setHeightFull();


        setMargin(true);

        add(chatWrapper, this.aside);

        this.tabs.addSelectedChangeListener(
                selectedChangeEvent -> {
                    Tab tab = selectedChangeEvent.getSelectedTab();
                    if(tab != null){
                        this.changeChat(tab.getLabel());
                    }
                }
        );

        Tab tab = this.tabs.getSelectedTab();
        if(tab != null){
            this.changeChat(tab.getLabel());
        }

        this.aside.getStyle().set("background-color", "#2c3d52");
        this.aside.getStyle().set("padding", "1rem");
        this.aside.getStyle().set("border-radius", "0.5rem");
        this.aside.getStyle().set("box-shadow", "0 0 0.5rem rgba(0, 0, 0, 0.1)");


        setHeight("95%");

    }



}
