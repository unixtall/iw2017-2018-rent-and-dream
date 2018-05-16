package es.uca.iw.rentAndDream.reserves;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.users.RoleType;

@SpringComponent
@UIScope
public class ReserveEditor extends VerticalLayout {
	
	private final ReserveService service;
	
	/**
	 * The currently edited user
	 */
	private Reserve reserve;

	private Binder<Reserve> binder = new Binder<>(Reserve.class);
	
	/* Fields to edit properties in Housing entity */
	TextField numberGuests = new TextField("Number Guest");
	DateField entryDate = new DateField("Entry Date");
	DateField departureDate = new DateField("Departure Date");
	TextField price = new TextField("Price");
	ComboBox<TypeReserveStatus> status = new ComboBox<TypeReserveStatus>("Status");
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);


	@Autowired
	public ReserveEditor(ReserveService service) {
		this.service = service;
		
		status.setItems(TypeReserveStatus.values());
		
		addComponents(numberGuests, entryDate, departureDate, price, status, actions);

		binder.forField(numberGuests)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
	  	.bind(Reserve::getNumberGuests, Reserve::setNumberGuests);
		
		binder.forField(price)
		.withConverter(
			new StringToFloatConverter("Must enter a number"))
	  	.bind(Reserve::getPrice, Reserve::setPrice);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(reserve));
		delete.addClickListener(e -> service.delete(reserve));
		cancel.addClickListener(e -> editReserve(reserve));
		setVisible(false);
		
		// Solo borra el admin
		delete.setEnabled(SecurityUtils.hasRole(RoleType.ADMIN));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editReserve(Reserve c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			reserve = service.findOne(c.getId());
		}
		else {
			reserve = c;
		}
		cancel.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(reserve);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		
		// Select all text in firstName field automatically
		//name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
}