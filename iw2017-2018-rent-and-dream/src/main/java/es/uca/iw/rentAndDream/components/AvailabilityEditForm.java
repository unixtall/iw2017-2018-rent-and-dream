package es.uca.iw.rentAndDream.components;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.AvailabilityService;
import es.uca.iw.rentAndDream.services.HousingService;

@SpringComponent
@Scope("prototype")
public class AvailabilityEditForm extends VerticalLayout{

	private Availability availability;
	
	private final AvailabilityService availabilityService;
	private final HousingService HousingService;

	private Binder<Availability> binder = new Binder<>(Availability.class);
	
	ComboBox<Housing> housing = new ComboBox<Housing>("Select one housing");
	DateField startDate = new DateField("Start Date");
	DateField endDate = new DateField("End Date");
	TextField price = new TextField("Price");
	Label validationStatus = new Label();
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, delete);
	
	@Autowired
	public AvailabilityEditForm(AvailabilityService availabilityService, HousingService housingService)
	{

		
		this.availabilityService = availabilityService;
		this.HousingService = housingService;
		
		this.availability = new Availability(null, null, 0f, null);
	
		delete.setEnabled(housing.getId() != null);
		
		if(SecurityUtils.hasRole(UserRoleType.ADMIN))
		{
			housing.setItems(housingService.findAll());
		}
		else
		{
			housing.setItems(housingService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
		}

		binder.forField(housing)
		.asRequired("is Required")
	  	.bind(Availability::getHousing, Availability::setHousing);
		
		binder.forField(price)
			.withConverter(
				new StringToFloatConverter("Must enter a number"))
			.asRequired("is Required")
		  	.bind(Availability::getPrice, Availability::setPrice);
		
		binder.forField(startDate)
		.withValidator(new DateRangeValidator("The start date is invalid", LocalDate.now(), LocalDate.now().plusYears(1)))
		.asRequired("is Required")
	  	.bind(Availability::getStartDate, Availability::setStartDate);
		
		binder.forField(endDate)
		.withValidator(new DateRangeValidator("The end date is invalid", LocalDate.now().plusDays(1), LocalDate.now().plusYears(1)))
		.asRequired("is Required")
	  	.bind(Availability::getEndDate, Availability::setEndDate);
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(availability);
		
		binder.bindInstanceFields(this);
		binder.setBean(availability);
		
		addComponents(housing, startDate, endDate, price, actions);
		
		binder.addStatusChangeListener(e -> {
			save.setEnabled(binder.isValid());
		});
		
		save.addClickListener(event-> {
			availabilityService.save(binder.getBean());
			Notification.show("Change sucessfull");
			save.setEnabled(false);
		});
		
		delete.addClickListener(event-> {
			availabilityService.delete(binder.getBean());
			Notification.show("Change sucessfull");
			binder.removeBean();
			delete.setEnabled(false);
		});
	}

	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability availability) {
		
		binder.setBean(availability);
		save.setEnabled(false);
		delete.setEnabled(true);
	}

	public Binder<Availability> getBinder() {
		return binder;
	}

	public void setBinder(Binder<Availability> binder) {
		this.binder = binder;
	}

	public ComboBox<Housing> getHousing() {
		return housing;
	}

	public void setHousing(ComboBox<Housing> housing) {
		this.housing = housing;
	}

	public DateField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateField startDate) {
		this.startDate = startDate;
	}

	public DateField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateField endDate) {
		this.endDate = endDate;
	}

	public TextField getPrice() {
		return price;
	}

	public void setPrice(TextField price) {
		this.price = price;
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
