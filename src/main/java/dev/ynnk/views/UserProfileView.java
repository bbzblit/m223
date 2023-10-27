package dev.ynnk.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.ynnk.MainLayout;
import dev.ynnk.model.EmailChange;
import dev.ynnk.model.User;
import dev.ynnk.service.EmailChangeService;
import dev.ynnk.service.HashService;
import dev.ynnk.service.MailService;
import dev.ynnk.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.apache.coyote.http11.Http11InputBuffer;

import java.util.UUID;


@Route(value = "profile", layout = MainLayout.class)
@RouteAlias(value = "profile", layout = MainLayout.class)
@PageTitle("Your Profile")
@PermitAll
public class UserProfileView extends VerticalLayout {

    private final UserService userService;

    private VerticalLayout formLayout;

    private HashService hashService;

    private final User currentUser;

    private final EmailChangeService emailChangeService;

    private final MailService mailService;

    public UserProfileView(
            final UserService userService,
            final HashService hashService,
            final EmailChangeService emailChangeService,
            final MailService  mailService){

        this.hashService = hashService;
        this.userService = userService;
        this.emailChangeService = emailChangeService;
        this.mailService = mailService;
        this.formLayout = new VerticalLayout();

        currentUser = this.userService.getCurrentLoggedInUser();

        TextField username = new TextField("Username");
        username.setValue(currentUser.getUsername());

        username.setReadOnly(true);

        EmailField email = new EmailField("Email");
        email.setValue(currentUser.getEmail());

        PasswordField password = new PasswordField("Password");

        PasswordField confirmPassword = new PasswordField("Confirm Password");

        H1 title = new H1("Edit your informations");

        Anchor anchor = new Anchor("", "Back to chat");

        Button save = new Button("Save");

        save.getStyle().set("cursor", "pointer");

        username.getStyle().set("width", "15rem");
        email.getStyle().set("width", "15rem");
        password.getStyle().set("width", "15rem");
        confirmPassword.getStyle().set("width", "15rem");

        save.addClickListener(
                event -> {
                    String pwd = this.currentUser.getPassword();

                    if (!password.isEmpty()){

                        if (!password.getValue().equals(confirmPassword.getValue())){
                            password.setErrorMessage("Passwords do not match");
                            password.setInvalid(true);
                            confirmPassword.setErrorMessage("Passwords do not match");
                            confirmPassword.setInvalid(true);
                            return;
                        }

                        pwd = this.hashService.hash(password.getValue());
                    }

                    if (!currentUser.getEmail().equals(email.getValue())){
                        this.emailChangeService.save(
                                EmailChange.builder()
                                        .newEmail(email.getValue())
                                        .user(currentUser)
                                        .id(UUID.randomUUID().toString())
                                        .build()
                        );


                    }

                    User user = User.builder()
                            .username(this.currentUser.getUsername())
                            .email(currentUser.getEmail())
                            .password(pwd)
                            .build();
                    this.userService.save(user);
                    Notification notification = new Notification();
                    notification.setText("Profile successfully updated");
                    notification.setDuration(3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();

                    getUI().ifPresent(ui -> ui.navigate(""));
                }
        );

        this.formLayout.add(username, email, password, confirmPassword, anchor ,save);

        add(title, this.formLayout);

        this.formLayout.setAlignItems(Alignment.CENTER);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setSizeFull();
    }


}
