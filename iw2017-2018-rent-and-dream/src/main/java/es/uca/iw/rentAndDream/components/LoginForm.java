package es.uca.iw.rentAndDream.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.UserService;

@SpringComponent
@Scope("prototype")
public class LoginForm extends VerticalLayout {


	private Binder<LoginForm> binder = new Binder<LoginForm>(LoginForm.class);

	@Autowired
	AuthenticationManager authenticationManager;
	
	UserService userService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String _username;
	private String _password;
	
	TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    Button loginButton = new Button("Login");
	Label validationStatus = new Label();
	
	@Autowired
	public LoginForm(UserService userService)
	{
		this.userService = userService;
			loginButton.addClickListener(evt -> {
	  
	            String pword = password.getValue();
	      		//System.out.println(form.getUsername.getValue() + " " + form.getPassword.getValue() + " " + pword);
	            if (!login(username.getValue(), pword)) {
	                Notification.show("Login failed");
	                password.setValue("");
	                username.focus();
	            }
	        });
			
			setMargin(true);
			setSpacing(true);
			
			loginButton.setEnabled(false);
		    loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
			
		    addComponents(username, password, loginButton);

		    binder.forField(username).asRequired("is Required").bind(s -> this._username, (s, v) -> this._username = v);
		    binder.forField(password).asRequired("is Required").bind(s -> this._password, (s, v) -> this._password = v);
		    
		    binder.setStatusLabel(validationStatus);
		    binder.addStatusChangeListener(event -> loginButton.setEnabled(binder.isValid()));
		    binder.bindInstanceFields(this);
		    //binder.setBean(this);
		}
	

private boolean login(String username, String password) {
	try {
		Authentication token = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		// Reinitialize the session to protect against session fixation
		// attacks. This does not work with websocket communication.
		
		VaadinService.reinitializeSession(VaadinService.getCurrentRequest());

		SecurityContextHolder.getContext().setAuthentication(token);

		//UI.getCurrent().setAttribute(User.class, userService.loadUserByUsername(username));
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(User.class.getName(), userService.findByUsername(username));
		

		
		Page.getCurrent().reload();
		
		
		return true;
	} catch (AuthenticationException ex) {
		return false;
		
	}
}

}
