package es.uca.iw.rentAndDream.users;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.reserves.Reserve;
import es.uca.iw.rentAndDream.security.SecurityUtils;

@SpringComponent
@UIScope
public class UserEditor extends FormLayout {
	
	private final UserService service;
	
	/**
	 * The currently edited user
	 */
	private User user;

	private Binder<User> binder = new Binder<>(User.class);
	
	/* Fields to edit properties in User entity */
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	TextField username = new TextField("Username");
	PasswordField passwordField = new PasswordField("Password");
	PasswordField confirmPasswordField = new PasswordField("Confirm Password");
	TextField email = new TextField("Email");
	DateField birthday = new DateField("Birthday");
	TextField dni = new TextField("Dni");
	TextField telephone = new TextField("Telephone");
	ComboBox <RoleType> role = new ComboBox <RoleType>("role");
	DateField registerDate = new DateField("Register Date");
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save,delete);

	@Autowired
	public UserEditor(UserService service) {
		this.service = service;
		
		// Add some items
		role.setItems(EnumSet.allOf(RoleType.class));
		role.setSelectedItem(RoleType.USER);
		registerDate.setValue(LocalDate.now());
		birthday.setPlaceholder("dd/mm/yy");

		addComponents(firstName, lastName, username, passwordField, confirmPasswordField, 
				email, birthday, dni, telephone, role, registerDate, actions);

		//Binders		
		binder.forField(firstName)
        	.asRequired("First name may not be empty")
        	.withValidator(
        		    name -> name.length() >= 2,
        		    "First name must contain at least two characters")
        	.withValidator(new RegexpValidator("First name can consist of alphabetical characters and only one space between them",
        			"^[a-zA-Z_]+( [a-zA-Z_]+)*$"))

        	.bind(User::getFirstName, User::setFirstName);
		
		binder.forField(lastName)
    		.asRequired("Last name may not be empty")
    		.withValidator(
        		    name -> name.length() >= 2,
        		    "Last name must contain at least two characters")
    		.withValidator(new RegexpValidator("Last name can consist of alphabetical characters and only one space between them",
        			"^[a-zA-Z_]+( [a-zA-Z_]+)*$"))
    		.bind(User::getLastName, User::setLastName);
		
		binder.forField(username)
    		.asRequired("Username may not be empty")
    		.withValidator(new StringLengthValidator(
    		        "Username must be between 3 and 15 characters long",
    		        3, 15))
    		.withValidator(new RegexpValidator("Username can consist of alphanumerical characters without spaces",
        			"^[a-zA-Z0-9_]*$"))
    		.bind(User::getUsername, User::setUsername);
		
		binder.forField(passwordField)
        	.asRequired("Password may not be empty")
        	.withValidator(new StringLengthValidator(
        			"Password must be at least 7 characters long", 7, null))
        	.bind(User::getPassword, User::setPassword);

		binder.forField(confirmPasswordField)
        	.asRequired("Must confirm password")
        	.withValidator(Validator.from(user -> {
    		    if (passwordField.isEmpty() || confirmPasswordField.isEmpty()) {
    		        return true;
    		    } else {
    		        return Objects.equals(passwordField.getValue(),
    		                confirmPasswordField.getValue());
    		    }
    		}, "Entered password and confirmation password must match"))
        	.bind(User::getPassword, (person, password) -> {});

		binder.forField(email)
		        .asRequired("Email may not be empty")
		        .withValidator(new EmailValidator("Not a valid email address"))
		        .bind(User::getEmail, User::setEmail);
		
		binder.forField(birthday)
			.asRequired("Birthday may not be empty")
			.withValidator(new DateRangeValidator("You must be over 18 years old", null, LocalDate.now().minusYears(18)))
			.bind(User::getBirthday, User::setBirthday);
		
		binder.forField(dni)
    		.asRequired("DNI name may not be empty")
    		.withValidator(new RegexpValidator("Username can consist of alphanumerical characters without spaces",
        			"^[a-zA-Z0-9_]*$"))
    		.bind(User::getDni, User::setDni);
		
		binder.forField(telephone)
			.withValidator(
	    		    phone -> phone.length() >= 9,
	    		    "Telephone must contain at least 9 characters")
			.asRequired("Telephone may not be empty")
			.withConverter(new StringToIntegerConverter("Must enter a number"))
			.bind(User::getTelephone, User::setTelephone);
		
		Label validationStatus = new Label();
		binder.setStatusLabel(validationStatus);
		
		//binder.getValidationStatusHandler();

		//binder.bindInstanceFields(this);
		
		binder.setBean(user);
 
        save.setEnabled(false);
        
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		save.addClickListener(
	              event -> {
	            	  try {
	            		  service.save(binder.getBean());
	            	  } catch (DataIntegrityViolationException e) {
	            	      Notification.show("Username or DNI already used, " +
	            	    	        "please use another username/dni");
	            	  }
	              });
 
        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));		
		
		
		

		// wire action buttons to delete and reset	
		delete.addClickListener(e -> service.delete(user));
		
		setVisible(false);
		
		// Solo borra el admin
		role.setVisible(SecurityUtils.hasRole(RoleType.ADMIN));
		registerDate.setVisible(SecurityUtils.hasRole(RoleType.ADMIN));
		delete.setVisible(SecurityUtils.hasRole(RoleType.ADMIN));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editUser(User c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			user = service.findOne(c.getId());
		}
		else {
			user = c;
		}

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(user);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		firstName.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
}