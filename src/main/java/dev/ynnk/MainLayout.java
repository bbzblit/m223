package dev.ynnk;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinServletRequest;
import dev.ynnk.service.HashService;
import dev.ynnk.service.SecurityService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    private void header(){
        HorizontalLayout title = new HorizontalLayout(VaadinIcon.CHAT.create(), new H1(" Chat"));
        title.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Button logout = new Button("Sign out", VaadinIcon.SIGN_OUT.create(), e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title, logout);



        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);


        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void footer(){
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.addClassNames("py-0", "px-m");


    }

    public MainLayout(final SecurityService securityService){
        this.securityService = securityService;
        header();
    }



}