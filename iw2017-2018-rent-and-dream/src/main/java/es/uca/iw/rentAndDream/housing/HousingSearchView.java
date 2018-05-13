package es.uca.iw.rentAndDream.housing;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.WelcomeView;
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
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	private DateField startDate;
	private DateField endDate;
	private ComboBox<Integer> guests;
	private List<Integer> rangoGuests;
	//private NativeSelect<Integer> price;
	//private NativeSelect<Integer> type;
	
	private HousingService housingService;
	private final CityService cityService;
	private final RegionService regionService;
	private final CountryService countryService;

	@Autowired
	public HousingSearchView(CountryService countryService, RegionService regionService, CityService cityService, HousingService housingService) {
		
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		this.housingService = housingService;
		this.filter = new TextField();
		this.search = new Button("Search");
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		this.startDate = new DateField("Select an entry date:");
		this.endDate = new DateField("Select an end date:");
		this.rangoGuests = IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList());
		this.guests = new ComboBox<>(null, rangoGuests);
	}
	
	@PostConstruct
	void init() throws NameNotFoundException {
		
		//Seteo para hacer mas facil el debug
		city.setItems(cityService.findOne(700044L));
		city.setSelectedItem(cityService.findOne(700044L));
		//
		country.setPlaceholder("Select a Country");
		country.setEmptySelectionAllowed(false);
		region.setPlaceholder("Select a Region");
		region.setEmptySelectionAllowed(false);
		city.setPlaceholder("Select a City");
		city.setEmptySelectionAllowed(false);
		guests.setPlaceholder("Number of guests");
		guests.setEmptySelectionAllowed(false);
		startDate.setValue(LocalDate.now());
		endDate.setValue(LocalDate.now().plusDays(1));
		
		guests.setValue(1);
		
		VerticalLayout filtrosBusqueda = new VerticalLayout(country, region, city, startDate, endDate, guests, search);
		filtrosBusqueda.setWidth("25%");
		
		GridLayout offers = new GridLayout();
		offers.setWidthUndefined();
		offers.setColumns(3);
		offers.setSpacing(true);
		
		country.addValueChangeListener(event -> region.setItems(countryService.findOne(event.getValue().getId()).getRegion()));
		region.addValueChangeListener(event -> city.setItems(regionService.findOne(event.getValue().getId()).getCities()));
		
		//Para que al pulsar el boton 'Search' se cargen todos los anuncios:
		search.addClickListener(event ->  addOffers(offers));
		
		HorizontalLayout items = new HorizontalLayout(filtrosBusqueda, offers);
		addComponents(items);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
	
	//añade las ofertas según los parámetros de la busqueda al grid
	private void addOffers(GridLayout gridLayout)
	{
		List<Housing> listaCasas = housingService.findByCityidAndAvailabilityAndGuest(city.getValue(), startDate.getValue(), endDate.getValue(), guests.getValue());
		gridLayout.removeAllComponents();
		listaCasas.forEach(e -> {
				VerticalLayout anuncio = new VerticalLayout();
				anuncio.addComponent(new Label("Tipo: habitación privada · " + e.getBeds() + " BEDS"));
				anuncio.addComponent(new Label(e.getName()));
				anuncio.addComponent(new Label("Desde 30€ por noche, stars: " + e.getAssessment()));
				anuncio.setWidth("250px");
				anuncio.addLayoutClickListener(event -> getUI().getNavigator().navigateTo(WelcomeView.VIEW_NAME));
				gridLayout.addComponent(anuncio);
			}
		);
	}
}