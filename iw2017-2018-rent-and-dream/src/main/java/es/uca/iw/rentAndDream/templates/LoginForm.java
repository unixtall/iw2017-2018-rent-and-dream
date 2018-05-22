package es.uca.iw.rentAndDream.templates;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.services.LoginService;

public class LoginForm extends VerticalLayout{

	private Binder<LoginForm> binder = new Binder<LoginForm>(LoginForm.class);

	private String _username;
	private String _password;
	TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    Button loginButton = new Button("Login");
	Label validationStatus = new Label();
	
	public LoginForm()
	{
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
	    binder.setBean(this);
	}

	public TextField getUsername() {
		return username;
	}

	public void setUsername(TextField username) {
		this.username = username;
	}

	public PasswordField getPassword() {
		return password;
	}

	public void setPassword(PasswordField password) {
		this.password = password;
	}

	public Button getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(Button loginButton) {
		this.loginButton = loginButton;
	}
	
}
