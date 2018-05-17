package es.uca.iw.rentAndDream.housing;

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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.users.RoleType;

@SpringComponent
@UIScope
public class HousingEditor extends VerticalLayout {
	
	private final HousingService service;
	
	/**
	 * The currently edited user
	 */
	private Housing housing;

	private Binder<Housing> binder = new Binder<>(Housing.class);
	
	/* Fields to edit properties in Housing entity */
	TextField name = new TextField("Name");
	TextField address = new TextField("Address");
	TextField assessment = new TextField("Calification");
	TextField description = new TextField("Description");
	TextField bedrooms = new TextField("Bedrooms");
	TextField beds = new TextField("Beds");
	CheckBox airConditioner = new CheckBox("airConditioner");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);


	@Autowired
	public HousingEditor(HousingService service) {
		this.service = service;

		addComponents(name, address, assessment, description, bedrooms, beds, airConditioner, actions);

		binder.forField(assessment)
			.withConverter(
				new StringToFloatConverter("Must enter a number"))
		  	.bind(Housing::getAssessment, Housing::setAssessment);
		
		binder.forField(bedrooms)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
	  	.bind(Housing::getBedrooms, Housing::setBedrooms);
		
		binder.forField(beds)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
	  	.bind(Housing::getBeds, Housing::setBeds);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(housing));
		delete.addClickListener(e -> service.delete(housing));
		cancel.addClickListener(e -> editHousing(housing));
		setVisible(false);
		
		// Solo borra el admin
		delete.setEnabled(SecurityUtils.hasRole(RoleType.ADMIN));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editHousing(Housing c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			housing = service.findOne(c.getId());
		}
		else {
			housing = c;
		}
		cancel.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(housing);

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
