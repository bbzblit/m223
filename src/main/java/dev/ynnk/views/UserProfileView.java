package dev.ynnk.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.ynnk.MainLayout;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.PermitAll;

@Route(value = "profile", layout = MainLayout.class)
@RouteAlias(value = "profile", layout = MainLayout.class)
@PageTitle("Your Profile")
@PermitAll
public class UserProfileView extends VerticalLayout {

    private final UserService userService;

    private FormLayout formLayout;


    public UserProfileView(final UserService userService){
        this.userService = userService;
        this.formLayout = new FormLayout();

        setSizeFull();
    }


}
