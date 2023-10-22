package dev.ynnk.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        TextField password = new TextField("Password");
        TextField confirmPassword = new TextField("Confirm Password");
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
            }
        });

        this.signUpForm.add(username, password, confirmPassword, signUp);
        this.signUpForm.setWidthFull();

        add(this.signUpForm);


    }

}
