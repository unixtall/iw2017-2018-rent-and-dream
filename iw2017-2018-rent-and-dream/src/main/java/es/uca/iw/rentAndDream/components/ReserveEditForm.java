package es.uca.iw.rentAndDream.components;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.ReserveService;
import es.uca.iw.rentAndDream.services.UserService;

@SpringComponent
@Scope("prototype")
public class ReserveEditForm extends VerticalLayout {
	
	private final ReserveService reserveService;
	private final UserService userService;
	private final HousingService housingService;
	
	
	/**
	 * The currently edited user
	 */
	private Reserve reserve;

	private Binder<Reserve> binder = new Binder<>(Reserve.class);
	
	/* Fields to edit properties in Housing entity */
	ComboBox<Housing> housing = new ComboBox<Housing>("Housing");
	ComboBox<User> user = new ComboBox<User>("User");
	TextField numberGuests = new TextField("Number Guest");
	DateField entryDate = new DateField("Entry Date");
	DateField departureDate = new DateField("Departure Date");
	TextField price = new TextField("Price");
	ComboBox<ReserveTypeStatus> status = new ComboBox<ReserveTypeStatus>("Status");
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, delete);


	@Autowired
	public ReserveEditForm(ReserveService reserveService, UserService userService, HousingService housingService) {
		this.reserveService = reserveService;
		this.userService = userService;
		this.housingService = housingService;
		
		this.reserve = new Reserve(0, null, null, 0f, null);
		
		this.setMargin(false);
		save.setEnabled(false);
		
		delete.setEnabled(reserve.getId() != null);
		user.setEnabled(false);
		price.setEnabled(false);
		status.setEnabled(false);
		housing.setEnabled(false);
		
		status.setItems(ReserveTypeStatus.values());
		
		if(SecurityUtils.hasRole(UserRoleType.ADMIN) || SecurityUtils.hasRole(UserRoleType.MANAGER))
		{
			user.setEnabled(true);
			housing.setEnabled(true);
			status.setEnabled(true);
			price.setEnabled(true);
			
			user.setItems(userService.findAll());
			housing.setItems(housingService.findAll());
		}
		
		addComponents(user, housing, numberGuests, entryDate, departureDate, price, status, actions);

		binder.forField(housing)
		.asRequired("Is required")
		.bind(Reserve::getHousing, Reserve::setHousing);
			
		binder.forField(user)
		.asRequired("Is required")
	  	.bind(Reserve::getUser, Reserve::setUser);
		
		binder.forField(numberGuests)
		.asRequired("Is required")
		.withConverter(
				new StringToIntegerConverter("Must enter a number"))
		.bind(Reserve::getNumberGuests, Reserve::setNumberGuests);
			
		binder.forField(price)
		.asRequired("Is required")
		.withConverter(
			new StringToFloatConverter("Must enter a number"))
	  	.bind(Reserve::getPrice, Reserve::setPrice);
		
		binder.forField(entryDate)
		.withValidator(new DateRangeValidator("The start date is invalid", LocalDate.now(), LocalDate.now().plusYears(1)))
		.asRequired("is Required")
	  	.bind(Reserve::getEntryDate, Reserve::setEntryDate);
		
		binder.forField(departureDate)
		.withValidator(new DateRangeValidator("The end date is invalid", LocalDate.now().plusDays(1), LocalDate.now().plusYears(1)))
		.asRequired("is Required")
	  	.bind(Reserve::getDepartureDate, Reserve::setDepartureDate);
		
		binder.forField(status)
		.asRequired("is Required")
	  	.bind(Reserve::getStatus, Reserve::setStatus);
		
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(reserve);
		
		binder.addStatusChangeListener(e -> {
			save.setEnabled(binder.isValid());
		});
		
		save.addClickListener(event-> {
			reserveService.save(binder.getBean());
			Notification.show("Change sucessfull");
			save.setEnabled(false);
		});
		
		delete.addClickListener(event-> {
			reserveService.delete(binder.getBean());
			Notification.show("Change sucessfull");
			binder.removeBean();
			delete.setEnabled(false);
		});
	}
	
	public Reserve getReserve() {
		return reserve;
	}

	public void setReserve(Reserve reserve) {
		binder.setBean(reserve);
		save.setEnabled(false);
		delete.setEnabled(true);
	}

	public ComboBox<Housing> getHousing() {
		return housing;
	}

	public void setHousing(ComboBox<Housing> housing) {
		this.housing = housing;
	}

	public ComboBox<User> getUser() {
		return user;
	}

	public void setUser(ComboBox<User> user) {
		this.user = user;
	}

	public TextField getNumberGuests() {
		return numberGuests;
	}

	public void setNumberGuests(TextField numberGuests) {
		this.numberGuests = numberGuests;
	}

	public DateField getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(DateField entryDate) {
		this.entryDate = entryDate;
	}

	public DateField getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(DateField departureDate) {
		this.departureDate = departureDate;
	}

	public TextField getPrice() {
		return price;
	}

	public void setPrice(TextField price) {
		this.price = price;
	}

	public ComboBox<ReserveTypeStatus> getStatus() {
		return status;
	}

	public void setStatus(ComboBox<ReserveTypeStatus> status) {
		this.status = status;
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



	public interface ChangeHandler {

		void onChange();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
}