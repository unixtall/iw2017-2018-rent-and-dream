package es.uca.iw.rentAndDream.housing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	private Button search;
	
	private Housing housing;
	private NativeSelect<Country> country; 
	private NativeSelect<Region> region; 
	private NativeSelect<City> city; 
	private DateTimeField entryDate;
	private DateTimeField endDate;
	private NativeSelect<String> guests;
	private List<String> rangoGuests;
	//private NativeSelect<Integer> price;
	//private NativeSelect<Integer> type;
	
	private HousingService housingService;
	private final CityService cityService;
	private final RegionService regionService;
	private final CountryService countryService;

	@Autowired
	public HousingSearchView(CountryService countryService, RegionService regionService, CityService cityService) {
		
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		
		this.grid = new Grid<>(Housing.class);
		this.filter = new TextField();
		this.search = new Button("Search");

		this.country = new NativeSelect<>("Select a country:", countryService.findAll());
		this.region = new NativeSelect<>("Select a region:");
		this.city = new NativeSelect<>("Select a city");
		this.entryDate = new DateTimeField("Select an entry date:");
		this.endDate = new DateTimeField("Select an end date:");
		this.rangoGuests = IntStream.range(1, 31).mapToObj(i -> i + " guest").collect(Collectors.toList());
		this.guests = new NativeSelect<>("Select the number of guests:", rangoGuests);
		
		entryDate.setValue(LocalDateTime.now());
		endDate.setValue(LocalDateTime.now());
	}
	
	@PostConstruct
	void init() throws NameNotFoundException {
		
		// build layout

		VerticalLayout filtrosBusqueda = new VerticalLayout(country, region, city, entryDate, endDate, guests, search);
		
		addComponents(filtrosBusqueda);

		grid.setSizeFull();
		grid.setColumns("id", "name", "assessment", "description", "bedrooms", "beds", "airConditioner");
		
		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		/*
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> {
			try {
				listHousingByCity(e.getValue());
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		*/
		country.addValueChangeListener(event -> region.setItems(countryService.findOne(event.getValue().getId()).getRegion()));
		
		region.addValueChangeListener(event -> city.setItems(regionService.findOne(event.getValue().getId()).getCities()));
		
		//Para añadir al grid los alojamientos que hay en la ciudad seleccionada de la lista de ciudades:
		city.addValueChangeListener(event -> grid.setItems(cityService.findOne(event.getValue().getId()).getHousing()));
		
/*
		// Instantiate and edit new User the new button is clicked
		search.addClickListener(e -> editor.editHousing(new Housing("", 0f, "", 0, 0, false)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listHousing(filter.getValue());
		});
*/
		// Initialize listing
		//listHousingByCity();
		
		//Para que al pulsar el boton 'Search' me muestre la tabla con los alojamientos:
		
		search.addClickListener(e -> addComponents(grid));
		
		//search.addClickListener(grid.setItems(cityService.loadCityByName(city.getValue().getName()).getHousing()));
	}
	
	/*
	private void listHousingByCity(String filterText) throws NameNotFoundException {
		if (StringUtils.isEmpty(filterText)) {
			//grid.setItems(service.findAll());
		} else {
			//grid.setItems(service.findByNameStartsWithIgnoreCase(filterText));
		}
	}
	*/
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}