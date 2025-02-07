package es.uca.iw.rentAndDream.components;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.CityService;

@SpringComponent
@UIScope
public class CityEditor extends VerticalLayout {
	
	private final CityService service;
	private CitySearchForm citySearchForm;
	
	/**
	 * The currently edited user
	 */
	private City city;

	private Binder<City> binder = new Binder<>(City.class);
	
	/* Fields to edit properties in City entity */
	TextField name = new TextField("Name");
	TextField latitude = new TextField("Latitude");
	TextField longitude = new TextField("Longitude");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);


	@Autowired
	public CityEditor(CityService service, CitySearchForm citySearchForm) {
		this.service = service;
		this.citySearchForm = citySearchForm;
		
		addComponents(citySearchForm, name, longitude, latitude, actions);

		binder.forField(citySearchForm.getCountry())
		.asRequired("Is required")
	  	.bind(City::getCountry, City::setCountry);
		
		binder.forField(citySearchForm.getRegion())
		.asRequired("Is required")
	  	.bind(City::getRegion, City::setRegion);
		
		binder.forField(latitude)
		.withConverter(
			new StringToFloatConverter("Must enter a number"))
	  	.bind(City::getLatitude, City::setLatitude);
		
		binder.forField(longitude)
		.withConverter(
			new StringToFloatConverter("Must enter a number"))
	  	.bind(City::getLongitude, City::setLongitude);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(city));
		delete.addClickListener(e -> service.delete(city));
		cancel.addClickListener(e -> editCity(city));
		setVisible(false);
		
		// Solo borra el admin
		delete.setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editCity(City c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			city = service.findOne(c.getId());
		}
		else {
			city = c;
		}
		cancel.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(city);

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
