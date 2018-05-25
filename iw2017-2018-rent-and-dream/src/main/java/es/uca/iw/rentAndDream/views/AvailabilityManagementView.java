package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.AvailabilityEditForm;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.services.AvailabilityService;
import es.uca.iw.rentAndDream.services.HousingService;

@SpringView(name = AvailabilityManagementView.VIEW_NAME)
public class AvailabilityManagementView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "availabilityManagementView";

	private Grid<Availability> grid;
	private TextField filter;
	private Button addNewBtn;

	private final AvailabilityService availabilityService;
	private final HousingService housingService;
	private AvailabilityEditForm availabilityEditForm;

	@Autowired
	public AvailabilityManagementView(AvailabilityService service,HousingService housingService, AvailabilityEditForm availabilityEditForm) {
		this.availabilityService = service;
		this.availabilityEditForm = availabilityEditForm;
		this.housingService = housingService;
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
		grid.setColumns("id", "housing", "startDate", "endDate", "price");

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
				availabilityEditForm.getSave().addClickListener(event -> 
				{
						
					 listAvailability(this.filter.getValue());
					 window.close();
				});			
				availabilityEditForm.getSave().addClickListener(event -> 
				{
					
					 listAvailability(this.filter.getValue());
					 window.close();
				});
			}
		});

		// Instantiate and edit new User the new button is clicked
		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e ->{
			availabilityEditForm.setAvailability(new Availability(null, null, 0f, null));
			
			Window window = new WindowManager("Housing Edit", availabilityEditForm).getWindow();
			
			window.addCloseListener(evt -> listAvailability(filter.getValue()) );
		});

		// Initialize listing
		listAvailability(null);

	}

	private void listAvailability(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(availabilityService.findAllWithHousing());
		} else {
			grid.setItems(availabilityService.findByHousingNameStartsWithIgnoreCaseWithHousing(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}