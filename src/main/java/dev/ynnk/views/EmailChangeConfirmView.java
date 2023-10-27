package dev.ynnk.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.ynnk.model.EmailChange;
import dev.ynnk.model.User;
import dev.ynnk.service.EmailChangeService;
import dev.ynnk.service.UserService;


@Route("email-change/confirm")
@RouteAlias(value = "email-change/confirm")
@PageTitle("Confirm Email Change")
@AnonymousAllowed
public class EmailChangeConfirmView extends VerticalLayout implements BeforeEnterObserver {

    private final EmailChangeService emailChangeService;

    private final UserService userService;

    public EmailChangeConfirmView(final EmailChangeService emailChangeService, final UserService userService){
        this.emailChangeService = emailChangeService;
    }

    private void changeEmail(String uuid){
        EmailChange emailChange = emailChangeService.findById(uuid);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        if(emailChange == null){
            add(new H1("Invalid or expired link"));
            return;
        }

        User user = emailChange.getUser();
        user.setEmail(emailChange.getNewEmail());

        userService.save(user);

        emailChangeService.deleteById(emailChange.getId());
        add(new H1("Email changed successfully"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String uuid = beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters().get("id").get(0);

        changeEmail(uuid);
    }

}
