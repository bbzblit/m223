package dev.ynnk.views;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import dev.ynnk.MainLayout;
import dev.ynnk.model.User;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.RolesAllowed;

import java.util.Collection;
import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "users", layout = MainLayout.class)
@PageTitle("User Overview")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class UserView extends VerticalLayout {

    private final UserService userService;

    Grid<User> userList = new Grid<>(User.class, false);


    public UserView(final UserService userService){
        this.userService = userService;

        userList.addColumn(User::getUsername).setHeader("Username");
        userList.addColumn(User::getEmail).setHeader("Email");
        userList.addColumn(User::isAdmin).setHeader("Role");

        for (User user : this.userService.findAll()) {
            userList.setItems(user);
        }


        add(userList);
    }



}
