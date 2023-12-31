package dev.ynnk;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Side;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinServletRequest;
import dev.ynnk.model.User;
import dev.ynnk.service.HashService;
import dev.ynnk.service.MailService;
import dev.ynnk.service.SecurityService;
import dev.ynnk.service.UserService;
import dev.ynnk.views.ChatView;
import dev.ynnk.views.UserProfileView;
import dev.ynnk.views.UserView;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class MainLayout extends AppLayout {

    private final UserService userService;

    private final SecurityService securityService;


    private final SideNav nav = new SideNav();

    private final User currentUser;

    private void header(){
        HorizontalLayout title = new HorizontalLayout(VaadinIcon.CHAT.create(), new H1(" Chat"));
        title.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Anchor titleLink = new Anchor("/", title);
        titleLink.setWidthFull();

        Button logout = new Button("Sign out", VaadinIcon.SIGN_OUT.create(), e -> securityService.logout());

        logout.getStyle().set("cursor", "pointer");

        Avatar avatar = new Avatar(this.currentUser.getUsername());
        avatar.setColorIndex((int)Math.floor(Math.random() * 7));

        Anchor profileLink = new Anchor("profile", avatar);

        avatar.getStyle().set("cursor", "pointer");

        HorizontalLayout header = new HorizontalLayout(profileLink, titleLink, logout);



        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);


        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

        initSideNav();
    }

    private void initSideNav(){

        SideNavItem home = new SideNavItem("Home", ChatView.class, VaadinIcon.HOME.create());
        SideNavItem profile = new SideNavItem("Profile", UserProfileView.class, VaadinIcon.USER.create());

        nav.addItem(home, profile);

        if (this.currentUser.isAdmin()){
            SideNavItem users = new SideNavItem("Users", UserView.class, VaadinIcon.USERS.create());
            nav.addItem(users);
        }

        addToDrawer(nav);
    }

    private void footer(){
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.addClassNames("py-0", "px-m");
    }


    public MainLayout(final UserService userService, final SecurityService securityService){


        this.userService = userService;
        this.securityService = securityService;

        this.currentUser = this.userService.getCurrentLoggedInUser();


        header();
    }



}
