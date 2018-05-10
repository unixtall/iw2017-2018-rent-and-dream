package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import org.springframework.util.StringUtils;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";

	private Grid<Housing> grid;
	private TextField filter;
	private Button buscar;
	private DateTimeField fechaEntrada;
	private DateTimeField fechaSalida;
	
    private HousingEditor editor;
	private final HousingService service;

	@Autowired
	public HousingSearchView(HousingService service, HousingEditor editor) {
		this.service = service;
		this.editor = editor;
		this.grid = new Grid<>(Housing.class);
		this.filter = new TextField();
		this.buscar = new Button("Buscar");
		this.fechaEntrada  = new DateTimeField();
		this.fechaSalida  = new DateTimeField();
		
		fechaEntrada.addValueChangeListener(event -> Notification.show("Fecha de entrada cambiada"));
		fechaSalida.addValueChangeListener(event -> Notification.show("Fecha de salida cambiada"));
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, buscar);
		VerticalLayout filtros = new VerticalLayout(fechaEntrada, fechaSalida);
		
		addComponents(actions, grid, filtros, editor);

		filter.setWidth(250, Unit.PIXELS);
		filter.setPlaceholder("¿Dónde te gustaría alojarte?");
		
		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("id", "name", "assessment", "description", "bedrooms", "beds", "airConditioner");


		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listHousing(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editHousing(e.getValue());
		});
/*
		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e -> editor.editHousing(new Housing("", 0f, "", 0, 0, false)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listHousing(filter.getValue());
		});
*/
		// Initialize listing
		listHousing(null);

	}

	private void listHousing(String filterText) {
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