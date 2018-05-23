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
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = ReserveHostView.VIEW_NAME)
public class ReserveHostView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "reserveHostView";

	private Grid<Reserve> grid;
	private TextField filter;
	private Button addNewBtn;


	
	private final ReserveService reserveService;

	@Autowired
	public ReserveHostView(ReserveService reserveService) {
		this.reserveService = reserveService;
		this.grid = new Grid<>(Reserve.class);
		this.filter = new TextField();  
		this.addNewBtn = new Button("New Availability", FontAwesome.PLUS);
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter);
		
		addComponents(actions, grid);

		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("housing", "entryDate", "departureDate", "price", "status");

		filter.setPlaceholder("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listReserve(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			if(e.getValue() != null)
			{
				Window window = new WindowManager("Availability Edit", reserveService.getEditForm(e.getValue())).getWindow();
				window.addCloseListener(evt -> listReserve(filter.getValue()) );
			}
		});



		// Initialize listing
		listReserve(null);

	}

	private void listReserve(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(reserveService.findAsHost((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
		} else {
			grid.setItems(reserveService.findByHousingAndHost(filterText, (User)VaadinService.getCurrentRequest()
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
