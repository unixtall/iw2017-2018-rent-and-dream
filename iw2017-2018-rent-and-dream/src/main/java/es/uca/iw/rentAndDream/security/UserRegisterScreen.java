package es.uca.iw.rentAndDream.security;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.VaadinUI;
import es.uca.iw.rentAndDream.WelcomeView;
import es.uca.iw.rentAndDream.users.RoleType;
import es.uca.iw.rentAndDream.users.User;
import es.uca.iw.rentAndDream.users.UserEditor.ChangeHandler;
import es.uca.iw.rentAndDream.users.UserService;

@SpringView(name = UserRegisterScreen.VIEW_NAME)
public class UserRegisterScreen extends VerticalLayout implements View  {

	public static final String VIEW_NAME = "userRegisterScreen";
	
	private User user; 
	private UserService service;
    private VaadinUI myUI;

	@PostConstruct
	void init() {		
				
        FormLayout layout = new FormLayout();
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setCaption("Registration Form");
 
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField username = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        TextField email = new TextField("Email");
    	DateField birthday = new DateField("Birthday");
    	TextField dni = new TextField("Dni");
    	TextField telephone = new TextField("Telephone");
 
        Binder<User> binder = new Binder<>();
 
        binder.forField(firstName)
        		.asRequired("First name may not be empty")
                .bind(User::getFirstName, User::setFirstName);
        
        binder.forField(lastName)
        		.asRequired("Last name may not be empty")
        		.bind(User::getLastName, User::setLastName);  
        
        binder.forField(username)
			.asRequired("Username may not be empty")
			.bind(User::getUsername, User::setUsername);
        
        binder.forField(passwordField)
        		.asRequired("Password may not be empty")
        		.withValidator(new StringLengthValidator(
        				"Password must be at least 7 characters long", 7, null))
        		.bind(User::getPassword, User::setPassword);

        binder.forField(confirmPasswordField)
        	.asRequired("Must confirm password")
        	.bind(User::getPassword, (user, password) -> {});
        
        binder.forField(email)
        	.asRequired("Email may not be empty")
        	.withValidator(new EmailValidator("Not a valid email address"))
        	.bind(User::getEmail, User::setEmail);
        
        binder.forField(birthday)
        		.asRequired("Birthday may not be empty")
        		.bind(User::getBirthday, User::setBirthday);
        
        binder.withValidator(Validator.from(user -> {
            if (passwordField.isEmpty() || confirmPasswordField.isEmpty()) {
                return true;
            } else {
                return Objects.equals(passwordField.getValue(),
                        confirmPasswordField.getValue());
            }
        }, "Entered password and confirmation password must match"));
 
        Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);        
        
        binder.setBean(user);
        
        Button save = new Button("Register");
        
        addComponents(firstName, lastName, username, passwordField, confirmPasswordField, email, birthday, dni, telephone, save);       
        
        //binder.bindInstanceFields(this);

        //save.setEnabled(false);
        save.addClickListener(e -> service.save(user));
    
        /*
        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));
        */

	}
	
	public interface ChangeHandler {

		void onChange();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	/*public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
	}*/
    
}
