package es.uca.iw.rentAndDream.templates;

import java.time.LocalDate;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;

public class ReserveEditForm extends VerticalLayout{
	
	private Binder<Reserve> binder = new Binder<>(Reserve.class);
	
	/* Fields to edit properties in Housing entity */
	ComboBox<Housing> housing = new ComboBox<Housing>("Housing");
	ComboBox<User> user = new ComboBox<User>("Status");
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

	public ReserveEditForm(Reserve r)
	{
		if(r == null)
			return;
		
		this.setMargin(false);
		save.setEnabled(false);
		delete.setEnabled(r.getId() != null);
		
		status.setItems(ReserveTypeStatus.values());
		price.setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
				
		binder.forField(numberGuests)
		.withConverter(
			new StringToIntegerConverter("Must enter a number"))
	  	.bind(Reserve::getNumberGuests, Reserve::setNumberGuests);
		
		binder.forField(price)
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
		
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(r);
		
		addComponents(user, housing, status, numberGuests, entryDate, departureDate, price, actions);

		
		binder.addStatusChangeListener(e -> {
			save.setEnabled(binder.isValid());
		});
	}

	public Binder<Reserve> getBinder() {
		return binder;
	}

	public void setBinder(Binder<Reserve> binder) {
		this.binder = binder;
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

	public CssLayout getActions() {
		return actions;
	}

	public void setActions(CssLayout actions) {
		this.actions = actions;
	}
	
	
}
