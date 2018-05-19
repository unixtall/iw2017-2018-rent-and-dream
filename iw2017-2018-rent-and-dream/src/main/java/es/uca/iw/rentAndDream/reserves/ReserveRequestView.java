package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.Editor;

import es.uca.iw.rentAndDream.users.UserService;

@SpringView(name = ReserveRequestView.VIEW_NAME)
public class ReserveRequestView extends CssLayout implements View {
	public static final String VIEW_NAME = "reserveRequestView";
	
	private Grid<Reserve> grid;

	private final ReserveService reserveService;
	private final UserService userService;
	private final ReserveEditor reserveEditor;

	@Autowired
	public ReserveRequestView(ReserveService reserveService, ReserveEditor reserveEditor, UserService userService) {
		this.reserveService = reserveService;
		this.reserveEditor = reserveEditor;
		this.userService = userService;
		this.grid = new Grid<>(Reserve.class);
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		CssLayout requestList = new CssLayout();
		
		addComponents(requestList, grid, reserveEditor);

		grid.setSizeFull();
		grid.setColumns("id", "numberGuests", "entryDate", "departureDate", "price", "status");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		//filter.setValueChangeMode(ValueChangeMode.LAZY);
		//filter.addValueChangeListener(e -> listReserve(Long.getLong(e.getValue())));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			reserveEditor.editReserve(e.getValue());
		});

		// Instantiate and edit new User the new button is clicked
		//addNewBtn.addClickListener(e -> editor.editReserve(new Reserve(0, null, null, 0f, null)));

		// Listen changes made by the editor, refresh data from backend
		//reserveEditor.setChangeHandler(() -> {
			//reserveEditor.setVisible(false);
			//listReserve(Long.getLong(filter.getValue()));
		//});

		// Initialize listing
		listReserve(null);

	}

	private void listReserve(Long filterText) {
		if (StringUtils.isEmpty(filterText)) {
			//grid.setItems(userService.findOne(arg0));
		} else {
			//grid.setItems(service.findByIdStartsWithIgnoreCase(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
