package es.uca.iw.rentAndDream.components;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.teemu.ratingstars.RatingStars;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.UserService;


@SpringComponent
@Scope("prototype")
public class HousingEditForm extends VerticalLayout {

	private Housing housing;
	
	private final UserService userService;
	private final HousingService housingService;
	private CitySearchForm citySearchForm;


	private Binder<Housing> binder = new Binder<>(Housing.class);
	
	ComboBox<User> user = new ComboBox<User>("User");
	TextField name = new TextField("Name");
	TextField address = new TextField("Address");
	RatingStars assessment = new RatingStars();
	RichTextArea description = new RichTextArea("Description");
	ComboBox<Integer> bedrooms = new ComboBox<Integer>("Bedrooms");
	ComboBox<Integer> beds = new ComboBox<Integer>("Beds");
	CheckBox airConditioner = new CheckBox("airConditioner");
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, delete);

	private Integer _bedrooms;

	private Integer _beds;
	
	@Autowired
	public HousingEditForm(UserService userService, HousingService housingService, CitySearchForm citySearchForm)
	{
		this.citySearchForm = citySearchForm;
		this.userService = userService;
		this.housingService = housingService;
		
		this.bedrooms = new ComboBox<>("Bedrooms", IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		this.beds = new ComboBox<>("Beds", IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		
		this.housing = new Housing("", "", 0d, "", 0, 0, false ,null, null);

		this.setMargin(false);
		save.setEnabled(false);
		user.setVisible(false);
		user.setEnabled(false);
		citySearchForm.setMargin(false);
		
		delete.setEnabled(housing.getId() != null);
		
		assessment.setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		assessment.setCaption("Calification");
		
		if(SecurityUtils.hasRole(UserRoleType.ADMIN))
		{
			user.setVisible(true);
			user.setEnabled(true);
			user.setItems(userService.findAll());
		}
		
		binder.forField(citySearchForm.getCity())
	  	.bind(Housing::getCity, Housing::setCity);
		
		binder.forField(user)
		.asRequired("Is required")
	  	.bind(Housing::getUser, Housing::setUser);
		
		binder.forField(name)
		.asRequired("Is required")
	  	.bind(Housing::getName, Housing::setName);
		
		binder.forField(assessment)
			/*.withConverter(
				new StringToDoubleConverter("Must enter a number"))*/
		  	.bind(Housing::getAssessment, Housing::setAssessment);
		
		binder.forField(address)
		.asRequired("Is required")
	  	.bind(Housing::getAddress, Housing::setAddress);
		
		binder.forField(description)
		.asRequired("Is required")
		.withValidator(new StringLengthValidator("Description must be 500 characters maximum", 1, 500))
	  	.bind(Housing::getDescription, Housing::setDescription);
		
		binder.forField(bedrooms)
		/*.withConverter(
			new StringToIntegerConverter("Must enter a number"))*/
		.asRequired("Is required")
		.withValidator(bedroom -> bedroom.intValue() > 0, "Bedrooms must be greater than 0")
		.bind(Housing::getBedrooms, Housing::setBedrooms);
		
		binder.forField(beds)
		/*.withConverter(
			new StringToIntegerConver
			ter("Must enter a number"))*/
		.asRequired("Is required")
		.bind(Housing::getBeds, Housing::setBeds);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(housing);
		addComponent(new HorizontalLayout(new VerticalLayout(user, citySearchForm, name, address, assessment)
				, new VerticalLayout(description, bedrooms, beds, airConditioner, actions)));
		
		binder.addStatusChangeListener(e -> {
			save.setEnabled(binder.isValid() && citySearchForm.getBinder().isValid());
		});
		
		citySearchForm.getBinder().addValueChangeListener(e-> {
			save.setEnabled(binder.isValid() && citySearchForm.getBinder().isValid());
		});
		
		save.addClickListener(event-> {
			housingService.save(binder.getBean());
			Notification.show("Change sucessfull");
			save.setEnabled(false);
		});
		
		delete.addClickListener(event-> {
			housingService.delete(binder.getBean());
			Notification.show("Change sucessfull");
			binder.removeBean();
			delete.setEnabled(false);
		});
	}

	public Housing getHousing() {
		return housing;
	}

	public void setHousing(Housing housing) {
		citySearchForm.getRegion().clear();
		citySearchForm.getCountry().clear();
		binder.setBean(housing);
		save.setEnabled(false);
		delete.setEnabled(true);
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

	/*public TextField getAssessment() {
		return assessment;
	}

	public void setAssessment(TextField assessment) {
		this.assessment = assessment;
	}*/

	public void setDescription(RichTextArea description) {
		this.description = description;
	}

	/*public TextField getBedrooms() {
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
	}*/

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

	public ComboBox<User> getUser() {
		return user;
	}

	public void setUser(User user) {
		this.housing.setUser(user);
		this.user.setItems(user);
		this.user.setSelectedItem(user);
	}
}
