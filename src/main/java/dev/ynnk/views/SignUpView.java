package dev.ynnk.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.ynnk.model.User;
import dev.ynnk.service.SecurityService;
import dev.ynnk.service.UserService;

@Route("signup")
@RouteAlias(value = "signup")
@PageTitle("Sign Up")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {

    private final SecurityService securityService;

    private final UserService userService;



    private final VerticalLayout signUpForm = new VerticalLayout();


    public SignUpView(final SecurityService securityService, final UserService userService){
        this.securityService = securityService;
        this.userService = userService;

        setSizeFull();

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        Button signUp = new Button("Sign Up");

        signUp.addClickListener(buttonClickEvent -> {

            if (this.userService.findById(username.getValue()) != null){
                return;
            }

            if(password.getValue().equals(confirmPassword.getValue())){
                String hashedPassword = this.securityService.hash(password.getValue());
                this.userService.save(User.builder()
                        .username(username.getValue())
                        .password(hashedPassword)
                        .build());
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
        });

        Anchor loginLink = new Anchor();
        loginLink.setText("Already have an account?");
        loginLink.setHref("login");

        this.signUpForm.add(username, password, confirmPassword, loginLink, signUp);
        this.signUpForm.setWidthFull();
        this.signUpForm.setAlignItems(Alignment.CENTER);
        H1 title = new H1("Register");

        add(title, this.signUpForm);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

}
