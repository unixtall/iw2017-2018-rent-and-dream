package es.uca.iw.rentAndDream.availabilities;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBooleanConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.users.RoleType;

@SpringComponent
@UIScope
public class AvailabilityEditor extends VerticalLayout {
	
	private final AvailabilityService service;
	
	/**
	 * The currently edited user
	 */
	private Availability availability;

	private Binder<Availability> binder = new Binder<>(Availability.class);
	
	/* Fields to edit properties in Availability entity */
	TextField startDate = new TextField("Start Date");
	TextField endDate = new TextField("End Date");
	TextField price = new TextField("Price");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);


	@Autowired
	public AvailabilityEditor(AvailabilityService service) {
		this.service = service;

		addComponents(startDate, endDate, price, actions);

		binder.forField(price)
			.withConverter(
				new StringToFloatConverter("Must enter a number"))
		  	.bind(Availability::getPrice, Availability::setPrice);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(availability));
		delete.addClickListener(e -> service.delete(availability));
		cancel.addClickListener(e -> editAvailability(availability));
		setVisible(false);
		
		// Solo borra el admin
		delete.setEnabled(SecurityUtils.hasRole(RoleType.ADMIN));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editAvailability(Availability c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			availability = service.findOne(c.getId());
		}
		else {
			availability = c;
		}
		cancel.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(availability);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
