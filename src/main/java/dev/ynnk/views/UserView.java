package dev.ynnk.views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import dev.ynnk.MainLayout;
import dev.ynnk.model.User;
import dev.ynnk.service.SecurityService;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "users", layout = MainLayout.class)
@PageTitle("User Overview")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class UserView extends VerticalLayout {

    private final UserService userService;

    Grid<User> userList = new Grid<>(User.class, false);

    private final User currentUser;

    private void initGrid(){

        Editor<User> editor = userList.getEditor();

        Grid.Column<User> usernameColumn = userList.addColumn(User::getUsername).setHeader("Username");
        Grid.Column<User> emailColumn = userList.addColumn(User::getEmail).setHeader("Email");
        Grid.Column<User> adminColumn = userList.addColumn(User::isAdmin).setHeader("Admin");

        Grid.Column<User> editColumn = userList.addComponentColumn(user -> {

            Button editButton = new Button("Edit");
            if (user.getUsername().equals(this.currentUser.getUsername())){
                editButton.setEnabled(false);
            }
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                userList.getEditor().editItem(user);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        Binder<User> binder = userList.getEditor().getBinder();

        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField usernameField = new TextField();
        usernameField.setReadOnly(true);
        binder.forField(usernameField).bind(User::getUsername, User::setUsername);
        usernameColumn.setEditorComponent(usernameField);

        TextField emailField = new TextField();
        binder.forField(emailField).bind(User::getEmail, User::setEmail);
        emailColumn.setEditorComponent(emailField);

        Checkbox adminCheckbox = new Checkbox();
        binder.forField(adminCheckbox).bind(User::isAdmin, User::setAdmin);
        adminColumn.setEditorComponent(adminCheckbox);
        adminColumn.setWidth("100px");

        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);

        editColumn.setEditorComponent(actions);
        editor.addSaveListener(
                event -> {
                    User user = event.getItem();
                    this.userService.save(user);
                }
        );

    }

    public UserView(final UserService userService, final SecurityService securityService){
        this.userService = userService;
        this.currentUser = userService.getCurrentLoggedInUser();


        initGrid();

        Iterable<User> dbUsers = this.userService.findAll();

        List<User> users = new ArrayList<>();

        dbUsers.forEach(users::add);

        userList.setItems(users);

        add(userList);
    }



}
