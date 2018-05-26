package es.uca.iw.rentAndDream.views;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.ReserveEditForm;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = ReserveManagementView.VIEW_NAME)
public class ReserveManagementView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "reserveManagementView";

	private Grid<Reserve> grid;
	private TextField filter;
	private Button addNewBtn;

	private ReserveEditForm reserveEditForm;
	
	private final ReserveService reserveService;

	@Autowired
	public ReserveManagementView(ReserveService reserveService, ReserveEditForm reserveEditForm) {
		this.reserveService = reserveService;
		this.reserveEditForm = reserveEditForm;
		this.grid = new Grid<>(Reserve.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New reserve", FontAwesome.PLUS);
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addComponents(actions, grid);

		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("id", "user", "housing", "numberGuests", "entryDate", "departureDate", "price", "status");

		grid.getColumn("user").setCaption("Tenant");

		filter.setPlaceholder("Filter by tenant");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listReserve(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			if(e.getValue() != null)
			{
				reserveEditForm.setReserve(e.getValue());
				Window window = new WindowManager("Reserves Edit", reserveEditForm).getWindow();
				
				reserveEditForm.getSave().addClickListener(event->{
					listReserve(filter.getValue());
					window.close();
				});

				reserveEditForm.getDelete().addClickListener(event->{
					listReserve(filter.getValue());
					window.close();
				});
			}
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e ->{
			
			reserveEditForm.setReserve(new Reserve(0, null, null, 0f, null));
			
			Window window = new WindowManager("Reserve Edit", reserveEditForm).getWindow();
			
			reserveEditForm.getSave().addClickListener(event->{
				listReserve(filter.getValue());
				window.close();
			});

			reserveEditForm.getDelete().addClickListener(event->{
				listReserve(filter.getValue());
				window.close();
			});
		});	

		// Initialize listing
		listReserve(null);

	}

	private void listReserve(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(reserveService.findAllWithHousingAndUser());
		} else {
			grid.setItems(reserveService.findByGuestUsername(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
