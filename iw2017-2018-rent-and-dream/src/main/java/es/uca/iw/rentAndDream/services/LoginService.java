package es.uca.iw.rentAndDream.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.templates.LoginForm;

@Service
public class LoginService {
  
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userService;
	
	private String _username;
	private String _password;

	
	private Binder<LoginService> binder = new Binder<LoginService>(LoginService.class);
	
	public VerticalLayout getLoginLayout()
	{

	    
		LoginForm form = new LoginForm();
        
		form.getLoginButton().addClickListener(evt -> {
  
            String pword = form.getPassword().getValue();
            form.getPassword().setValue("");
            
      		//System.out.println(form.getUsername.getValue() + " " + form.getPassword.getValue() + " " + pword);
            if (!login(form.getUsername().getValue(), pword)) {
                Notification.show("Login failed");
                form.getUsername().focus();
            }
        });

        return form;
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
	
    /**
  	 * 
  	 */
  	private static final long serialVersionUID = 5304492736395275231L;
}
