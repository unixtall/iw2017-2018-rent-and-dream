package es.uca.iw.rentAndDream.users;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.reserves.Reserve;
import es.uca.iw.rentAndDream.security.SecurityUtils;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout {
	
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
	TextField password = new TextField("Password");
	TextField email = new TextField("Email");
	DateField birthday = new DateField("Birthday");
	TextField dni = new TextField("Dni");
	TextField telephone = new TextField("Telephone");
	ComboBox <RoleType> role = new ComboBox <RoleType>("role");
	//RoleType role;
	DateField registerDate = new DateField("Register Date");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);


	@Autowired
	public UserEditor(UserService service) {
		this.service = service;

			
		// Add some items
		role.setItems(EnumSet.allOf(RoleType.class));

		// Show 3 items and a scrollbar if there are more
		//roleList.setRows(3);
		
//		roleList.addValueChangeListener(event -> {
//		    RoleType role = event.getValue().stream().findFirst().get();
//		});

		addComponents(firstName, lastName, username, password, email, birthday, dni, telephone, role, registerDate, actions);

		binder.forField(telephone)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
	  	.bind(User::getTelephone, User::setTelephone);
		
		// bind using naming convention
		binder.bindInstanceFields(this);

		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(user));
		delete.addClickListener(e -> service.delete(user));
		cancel.addClickListener(e -> editUser(user));
		setVisible(false);
		
		// Solo borra el admin
		delete.setEnabled(SecurityUtils.hasRole(RoleType.ADMIN));
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
		cancel.setVisible(persisted);

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