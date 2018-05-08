package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = ReserveManagementView.VIEW_NAME)
public class ReserveManagementView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "reserveManagementView";

	private Grid<Reserve> grid;
	private TextField filter;
	private Button addNewBtn;

	private ReserveEditor editor;
	
	private final ReserveService service;

	@Autowired
	public ReserveManagementView(ReserveService service, ReserveEditor editor) {
		this.service = service;
		this.editor = editor;
		this.grid = new Grid<>(Reserve.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New reserve", FontAwesome.PLUS);
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addComponents(actions, grid, editor);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "numberGuests", "entryDate", "departureDate", "price", "confirmed");

		filter.setPlaceholder("Filter by id");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listReserve(Long.getLong(e.getValue())));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editReserve(e.getValue());
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e -> editor.editReserve(new Reserve(0, null, null, 0f, false)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listReserve(Long.getLong(filter.getValue()));
		});

		// Initialize listing
		listReserve(null);

	}

	private void listReserve(Long filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findAll());
		} else {
			//grid.setItems(service.findByIdStartsWithIgnoreCase(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
