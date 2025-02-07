package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.AvailabilityEditForm;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.AvailabilityService;

@ViewScope
@SpringView(name = AvailabilityUserView.VIEW_NAME)
public class AvailabilityUserView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "availabilityUserView";

	private Grid<Availability> grid;
	private TextField filter;
	private Button addNewBtn;

	private final AvailabilityService availabilityService;
	private final AvailabilityEditForm availabilityEditForm;


	@Autowired
	public AvailabilityUserView(AvailabilityService service, AvailabilityEditForm availabilityEditForm) {
		this.availabilityService = service;
		this.availabilityEditForm = availabilityEditForm;
		this.grid = new Grid<>(Availability.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New Availability", FontAwesome.PLUS);
	    
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addComponents(actions, grid);

		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("housing", "startDate", "endDate", "price");
		grid.getColumn("price").setCaption("Price per day");
		filter.setPlaceholder("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listAvailability(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			if(e.getValue() != null)
			{
				availabilityEditForm.setAvailability(e.getValue());
				Window window = new WindowManager("Availability Edit", availabilityEditForm).getWindow();
				
				availabilityEditForm.getSave().addClickListener(event->{
					listAvailability(filter.getValue());
					window.close();
				});

				availabilityEditForm.getDelete().addClickListener(event->{
					listAvailability(filter.getValue());
					window.close();
				});
			}
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e ->{
			
			availabilityEditForm.setAvailability(new Availability(null, null, 0f, null));
			Window window = new WindowManager("Housing Edit", availabilityEditForm).getWindow();
			
			availabilityEditForm.getSave().addClickListener(event->{
				listAvailability(filter.getValue());
				window.close();
			});

			availabilityEditForm.getDelete().addClickListener(event->{
				listAvailability(filter.getValue());
				window.close();
			});
		});

		// Initialize listing
		listAvailability(null);

	}

	private void listAvailability(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(availabilityService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
		} else {
			grid.setItems(availabilityService.findByUserAndHousingName((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName()), filterText));
		}
	}
	
	
	
	public TextField getFilter() {
		return filter;
	}

	public void setFilter(TextField filter) {
		this.filter = filter;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}