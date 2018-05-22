package es.uca.iw.rentAndDream.templates;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;

public class HousingEditForm extends VerticalLayout {

	private Housing housing;

	private Binder<Housing> binder = new Binder<>(Housing.class);
	
	TextField name = new TextField("Name");
	TextField address = new TextField("Address");
	TextField assessment = new TextField("Calification");
	TextField description = new TextField("Description");
	TextField bedrooms = new TextField("Bedrooms");
	TextField beds = new TextField("Beds");
	CheckBox airConditioner = new CheckBox("airConditioner");
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, delete);
		
	public HousingEditForm(Housing h)
	{
		if(h == null)
			return;
		
		this.setMargin(false);
		save.setEnabled(false);
		delete.setEnabled(h.getId() != null);
		assessment.setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		

		
		binder.forField(name)
		.asRequired("Is required")
	  	.bind(Housing::getName, Housing::setName);
		
		binder.forField(assessment)
			.withConverter(
				new StringToFloatConverter("Must enter a number"))
			.asRequired("Is required")
		  	.bind(Housing::getAssessment, Housing::setAssessment);
		
		binder.forField(address)
		.asRequired("Is required")
	  	.bind(Housing::getAddress, Housing::setAddress);
		
		binder.forField(description)
		.asRequired("Is required")
	  	.bind(Housing::getDescription, Housing::setDescription);
		
		binder.forField(bedrooms)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
		.asRequired("Is required")
	  	.bind(Housing::getBedrooms, Housing::setBedrooms);
		
		binder.forField(beds)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
		.asRequired("Is required")
	  	.bind(Housing::getBeds, Housing::setBeds);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(h);
		addComponents(name, address, assessment, description, bedrooms, beds, airConditioner, actions);
		
		binder.addStatusChangeListener(e -> {
			save.setEnabled(binder.isValid());
		});
	}

	public Housing getHousing() {
		return housing;
	}

	public void setHousing(Housing housing) {
		this.housing = housing;
	}

	public Binder<Housing> getBinder() {
		return binder;
	}

	public void setBinder(Binder<Housing> binder) {
		this.binder = binder;
	}

	public TextField getName() {
		return name;
	}

	public void setName(TextField name) {
		this.name = name;
	}

	public TextField getAddress() {
		return address;
	}

	public void setAddress(TextField address) {
		this.address = address;
	}

	public TextField getAssessment() {
		return assessment;
	}

	public void setAssessment(TextField assessment) {
		this.assessment = assessment;
	}
 

	public void setDescription(TextField description) {
		this.description = description;
	}

	public TextField getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(TextField bedrooms) {
		this.bedrooms = bedrooms;
	}

	public TextField getBeds() {
		return beds;
	}

	public void setBeds(TextField beds) {
		this.beds = beds;
	}

	public CheckBox getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(CheckBox airConditioner) {
		this.airConditioner = airConditioner;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public Button getDelete() {
		return delete;
	}

	public void setDelete(Button delete) {
		this.delete = delete;
	}

	public CssLayout getActions() {
		return actions;
	}

	public void setActions(CssLayout actions) {
		this.actions = actions;
	}
	
	

}
