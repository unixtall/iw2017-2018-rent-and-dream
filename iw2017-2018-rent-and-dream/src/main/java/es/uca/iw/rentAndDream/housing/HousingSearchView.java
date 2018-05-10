package es.uca.iw.rentAndDream.housing;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.cities.CityService;
import es.uca.iw.rentAndDream.countries.Country;
import es.uca.iw.rentAndDream.countries.CountryService;
import es.uca.iw.rentAndDream.regions.Region;
import es.uca.iw.rentAndDream.regions.RegionService;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";

	private Grid<Housing> grid;
	private TextField filter;
	private Button buscar;
	
	private NativeSelect<Country> country; 
	private NativeSelect<Region> region; 
	private NativeSelect<City> city; 

	private DateTimeField fechaEntrada;
	private DateTimeField fechaSalida;

	private HousingEditor editor;
	
	private final CityService service;
	private final RegionService regionService;
	private final CountryService countryService;

	@Autowired
	public HousingSearchView(CityService service, HousingEditor editor, CountryService countryService, RegionService regionService, CityService cityService) {
		this.service = service;
		this.regionService = regionService;
		this.countryService = countryService;
		this.editor = editor;
		this.grid = new Grid<>(Housing.class);
		this.filter = new TextField();
		this.buscar = new Button("Buscar");
		this.fechaEntrada = new DateTimeField();
		this.fechaSalida = new DateTimeField();
		this.country = new NativeSelect<>("Seleccione un pais", countryService.findAll());
		this.region = new NativeSelect<>("Seleccione una region");
	//	this.city = new NativeSelect<>("Seleccione una ciudad", cityService.findAll());
		
		fechaEntrada.setValue(LocalDateTime.now());
		fechaEntrada.addValueChangeListener(event -> Notification.show("Fecha entrada cambiada"));
	    
		fechaSalida.setValue(LocalDateTime.now());
		fechaSalida.addValueChangeListener(event -> Notification.show("Fecha salida cambiada"));
	}
	
	@PostConstruct
	void init() throws NameNotFoundException {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, buscar);
		VerticalLayout filtrosBusqueda = new VerticalLayout(fechaEntrada, fechaSalida, country, region);
		
		addComponents(actions, filtrosBusqueda, grid, editor);

		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("id", "name", "assessment", "description", "bedrooms", "beds", "airConditioner");

		filter.setPlaceholder("¿Dónde te gustaría alojarte?");
		filter.setWidth(250, Unit.PIXELS);

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> {
			try {
				listHousingByCity(e.getValue());
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		country.addValueChangeListener(event -> region.setItems(countryService.findOne(event.getValue().getId()).getRegion()));

/*
		// Instantiate and edit new User the new button is clicked
		buscar.addClickListener(e -> editor.editHousing(new Housing("", 0f, "", 0, 0, false)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listHousing(filter.getValue());
		});
*/
		// Initialize listing
		listHousingByCity("Fucking");

	}

	private void listHousingByCity(String filterText) throws NameNotFoundException {
		if (StringUtils.isEmpty(filterText)) {
			//grid.setItems(service.findAll());
		} else {
			//grid.setItems(service.findByNameStartsWithIgnoreCase(filterText));
			grid.setItems(service.loadCityByName(filterText).getHousing());
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}