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
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.ReserveEditForm;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = ReserveHostView.VIEW_NAME)
public class ReserveHostView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "reserveHostView";

	private Grid<Reserve> grid;
	private TextField filter;
	private Button addNewBtn;

	private final ReserveService reserveService;
	private ReserveEditForm reserveEditForm;

	@Autowired
	public ReserveHostView(ReserveService reserveService, ReserveEditForm reserveEditForm) {
		this.reserveService = reserveService;
		this.reserveEditForm = reserveEditForm;
		this.grid = new Grid<>(Reserve.class);
		this.filter = new TextField();  
		this.addNewBtn = new Button("New Availability", FontAwesome.PLUS);
	}
	
	@PostConstruct
	void init() {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addNewBtn.setVisible(SecurityUtils.hasRole(UserRoleType.ADMIN) || SecurityUtils.hasRole(UserRoleType.MANAGER));

		addComponents(actions, grid);
		
		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("user", "housing", "numberGuests", "entryDate", "departureDate", "price", "status");
		grid.getColumn("user").setCaption("Tenant");
		filter.setPlaceholder("Filter by Housing");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listReserve(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			if(e.getValue() != null)
			{
				reserveEditForm.setReserve(e.getValue());
				
				if(e.getValue().getStatus() == ReserveTypeStatus.PENDING)
				{
					Button confirmButton = new Button("Confirm reserve");
					Button canceledButton = new Button("Cancel reserve");
					
					Window window = new WindowManager("ReserveHost Edit", new VerticalLayout(confirmButton, canceledButton)).getWindow();
					
					confirmButton.addClickListener(event-> {
						e.getValue().setStatus(ReserveTypeStatus.CONFIRMED);
						//reserveService.save(e.getValue());
						reserveService.confirm(e.getValue());
						listReserve(filter.getValue());
						window.close();
					});
					
				
					canceledButton.addClickListener(event-> {
						e.getValue().setStatus(ReserveTypeStatus.CANCELEDBYHOST);
						reserveService.save(e.getValue());
						listReserve(filter.getValue());
						window.close();
					});
				
	
					reserveEditForm.getSave().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
					reserveEditForm.getDelete().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
				}

				if(e.getValue().getStatus() == ReserveTypeStatus.CONFIRMED)
				{	
					Button finishedButton = new Button("Finish reserve");
					Button canceledButton = new Button("Cancel reserve");
					Window window = new WindowManager("ReserveHost Edit", new VerticalLayout(finishedButton , canceledButton)).getWindow();
					
					finishedButton.addClickListener(event-> {
						e.getValue().setStatus(ReserveTypeStatus.FINISHED);
						reserveService.save(e.getValue());
						listReserve(filter.getValue());
						window.close();
					});					
					
					canceledButton.addClickListener(event-> {
						e.getValue().setStatus(ReserveTypeStatus.CANCELEDBYHOST);
						reserveService.save(e.getValue());
						listReserve(filter.getValue());
						window.close();
					});
				
					reserveEditForm.getSave().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
					reserveEditForm.getDelete().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
				}
				
				
				
			}
		});



		// Initialize listing
		listReserve(null);
		
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
		

	}

	private void listReserve(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(reserveService.findAsHost((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
		} else {
			grid.setItems(reserveService.findByHousingNameAndAsHost(filterText, (User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
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
