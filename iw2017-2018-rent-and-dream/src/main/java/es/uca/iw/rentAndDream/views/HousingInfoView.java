package es.uca.iw.rentAndDream.views;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.tuningdatefield.TuningDateField;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.LoginForm;
import es.uca.iw.rentAndDream.components.RangeSelectDateField;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = HousingInfoView.VIEW_NAME)
public class HousingInfoView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "housingInfoView";
	
	private Binder<Housing> binder = new Binder<>(Housing.class);
	
	private Housing housing;	
	private HousingService housingService;
	private ReserveService reserveService;
	private CityService cityService;
	
	@Autowired
	private LoginForm loginForm;
	
	RangeSelectDateField rangeSelectDateField;
	
	Label entryDate = new Label();
	Label departureDate = new Label();
	ComboBox<Integer> guests = new ComboBox<>("Guests");
	Label price = new Label();
	Button reserveButton = new Button("Reserve Now");
	
	private String[] uriTokens;
	
	private TuningDateField availabilityDateField = new TuningDateField("Availability Calendar");	
	
	
	@PostConstruct
	public void init() {
	
	    entryDate.setCaption("Entry Date:");
	    departureDate.setCaption("Departure Date:");
	    price.setCaption("Price:");
	    this.guests.setEmptySelectionAllowed(false);

	  }
	
	@Autowired
	public HousingInfoView(HousingService housingService, ReserveService reserveService, CityService cityService, RangeSelectDateField rangeSelectDateField) {
		this.housingService = housingService;
		this.reserveService = reserveService;
		this.cityService = cityService;
		//this.loginForm = loginForm;
		this.rangeSelectDateField = rangeSelectDateField;
		this.guests.setSizeUndefined();


	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		uriTokens = Page.getCurrent().getUriFragment().split("/");
		

		if(uriTokens.length != 2)
			return;

		Long idHousing = Long.parseLong(uriTokens[1]);
		
		housing = housingService.findOneWithAvailabilityAndCityAndUser(idHousing);

		
		if(housing == null)
		{
			addComponent(new Label("Housing not found"));
			return;
		}

		
		guests = new ComboBox<>("Guests", IntStream.range(1, housing.getBeds() + 1).mapToObj(i -> i).collect(Collectors.toList()));
		guests.setSelectedItem(1);
		guests.setRequiredIndicatorVisible(true);
		
		HorizontalLayout features = new HorizontalLayout();
		Label name = new Label(housing.getName() + " Rating: " + housing.getAssessment());
		Label city = new Label(housing.getCity().getName());
		Label beds = new Label(housing.getBeds().toString() + " Beds");
		Label bedrooms = new Label(housing.getBedrooms().toString() + " Bedrooms");
		Label airAconditioner = new Label();
		RichTextArea description = new RichTextArea();
		
		//añadimos las fechas disponibles para alquilar
		Set<LocalDate> datesForAvailability = new HashSet<>();
		housing.getAvailability().forEach(e->{
			//a.add(e)
			LocalDate d;
			for(d = e.getStartDate(); !d.isEqual(e.getEndDate().plusDays(1)); d = d.plusDays(1) )
			{
				if(d.isAfter(LocalDate.now().minusDays(1)))
					if(SecurityUtils.isLoggedIn())
					{
						if(!housingService.isReservedWithUserPendingReserve(d, housing, (User)(VaadinService.getCurrentRequest()
							    .getWrappedSession().getAttribute(User.class.getName()))))
							datesForAvailability.add(d);
					}
					else {
						if(!housingService.isReserved(d, housing))
							datesForAvailability.add(d);
					}
				
			}
		});
		
		rangeSelectDateField.setAvailabilyDates(datesForAvailability);
		reserveButton.setEnabled(false);
		
		guests.addValueChangeListener(e -> {
			reserveButton.setEnabled(rangeSelectDateField.getStartDate() != null && !guests.isEmpty());
		});
		
		rangeSelectDateField.getInlineTuningDateField().addDateChangeListener(e-> 
		{
			//Notification.show("Values Dates:" + rangeSelectDateField.getValue() + " stardate:" + rangeSelectDateField.getStartDate() + " enddate:" + rangeSelectDateField.getEndDate());
			if(rangeSelectDateField.getStartDate() != null)
			{
				this.entryDate.setValue(rangeSelectDateField.getStartDate().toString());
				if(rangeSelectDateField.getEndDate() == null)
					this.departureDate.setValue(rangeSelectDateField.getStartDate() != null ? rangeSelectDateField.getStartDate().plusDays(1).toString() : null);
				else
					this.departureDate.setValue(rangeSelectDateField.getEndDate().toString());
				
				Float housingPrice = reserveService.calculatePrice(rangeSelectDateField.getStartDate()
						, rangeSelectDateField.getEndDate() == null ? rangeSelectDateField.getStartDate().plusDays(1) 
						: rangeSelectDateField.getEndDate(), housing);
				
				Float totalPrice = housingPrice + (housingPrice * cityService.findByHousing(housing).getCountry().getVat());
				Float vatAmount = totalPrice - housingPrice ;
				
				price.setValue(housingPrice.toString() + " + " + String.format("%.2f", vatAmount) + "(VAT) = " + totalPrice );
				reserveButton.setEnabled(rangeSelectDateField.getStartDate() != null && !guests.isEmpty());
			}
		});
		
		

		reserveButton.addClickListener(e->{
			if(SecurityUtils.isLoggedIn())
			{
				ConfirmDialog.show(getUI().getCurrent(), "You are Sure?", new ConfirmDialog.Listener() {
					
		            public void onClose(ConfirmDialog dialog) {
		            	
						if(dialog.isConfirmed())
						{
							Reserve r = new Reserve(guests.getValue()
									, LocalDate.parse(entryDate.getValue())
									, LocalDate.parse(departureDate.getValue()), null, ReserveTypeStatus.PENDING);
							r.setUser((User)(VaadinService.getCurrentRequest()
								    .getWrappedSession().getAttribute(User.class.getName())));
							r.setHousing(housing);
							reserveService.save(r);
							Notification.show("Reserve Request SucessFull");
						}
		            }
		        });
				

			}
			else
			{
				Window window = new WindowManager("Enter to you account", loginForm).getWindow();
			}

		});
		
		
		
		airAconditioner.setCaption(" Air Aconditioner");
		airAconditioner.setIcon(housing.getAirConditioner()? VaadinIcons.CHECK_CIRCLE_O : VaadinIcons.CLOSE_CIRCLE_O);
		airAconditioner.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
		description.setValue(housing.getDescription());
		description.setReadOnly(true);
		rangeSelectDateField.setWidth("50%");	
		
		//Calculamos el precio mas bajo de entre las disponibilidades
		Float lowPrice = Float.POSITIVE_INFINITY;
		for(Availability a : housing.getAvailability())
			if(a.getPrice() < lowPrice)
				lowPrice = a.getPrice();	
		
		Label fromPrice = new Label("From " + lowPrice + "€");

		
        name.setStyleName(ValoTheme.LABEL_H3);
        city.setStyleName(ValoTheme.LABEL_H4);
		
		features.addComponents(beds, bedrooms, airAconditioner, fromPrice);

		features.setWidth("80%");
		
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		HorizontalLayout splitLayout = new HorizontalLayout();
		splitLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		splitLayout.addComponent(description);
		VerticalLayout datesLayout = new VerticalLayout(entryDate, departureDate);
		datesLayout.setMargin(false);
		VerticalLayout reserveInfoLayout = new VerticalLayout(guests, price);
		reserveInfoLayout.setMargin(false);
		VerticalLayout reserveLayout = new VerticalLayout(rangeSelectDateField, new HorizontalLayout(datesLayout, reserveInfoLayout), reserveButton);
		
		if(SecurityUtils.isLoggedIn())
		{
			User loggedUser = (User)(VaadinService.getCurrentRequest()
				    .getWrappedSession().getAttribute(User.class.getName()));
			
			reserveLayout.setEnabled(loggedUser.getId() != housing.getUser().getId());
			//Notification.show("User logged: " + loggedUser.getId()  + " Housing own: " + housing.getUser().getId());
		}
		
		
		reserveLayout.setSizeUndefined();

		splitLayout.addComponent(reserveLayout);
		splitLayout.setComponentAlignment(reserveLayout, Alignment.MIDDLE_CENTER);
		//splitLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		splitLayout.setSizeFull();
		addComponents(name, city, features, splitLayout);
		setSizeFull();
	}
}
