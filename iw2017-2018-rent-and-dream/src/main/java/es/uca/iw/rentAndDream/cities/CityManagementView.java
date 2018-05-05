package es.uca.iw.rentAndDream.cities;

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

@SpringView(name = CityManagementView.VIEW_NAME)
public class CityManagementView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "cityManagementView";

	private Grid<City> grid;
	private TextField filter;
	private Button addNewBtn;

	private CityEditor editor;

	private final CityService service;

	@Autowired
	public CityManagementView(CityService service, CityEditor editor) {
		this.service = service;
		this.editor = editor;
		this.grid = new Grid<>(City.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New City", FontAwesome.PLUS);
	    
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addComponents(actions, grid, editor);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "name");

		filter.setPlaceholder("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listCity(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editCity(e.getValue());
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCity(new City("", "", 0)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCity(filter.getValue());
		});

		// Initialize listing
		listCity(null);

	}

	private void listCity(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findAll());
		} else {
			grid.setItems(service.findByNameStartsWithIgnoreCase(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
