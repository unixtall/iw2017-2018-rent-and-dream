package es.uca.iw.rentAndDream.housing;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.availabilities.Availability;
import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.cities.CityService;
import es.uca.iw.rentAndDream.countries.Country;
import es.uca.iw.rentAndDream.countries.CountryService;
import es.uca.iw.rentAndDream.regions.Region;
import es.uca.iw.rentAndDream.regions.RegionService;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	private Binder<HousingSearchView> binder = new Binder<HousingSearchView>(HousingSearchView.class);
	
	private final HousingService housingService;
	private final CityService cityService;
	private final RegionService regionService;
	private final CountryService countryService;
	
	private Country _country;
	private Region _region;
	private City _city;
	private Integer _guests;
	private LocalDate _startDate;
	private LocalDate _endDate;

	//Componentes del layout	
	private CssLayout offers;	
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	private DateField startDate;
	private DateField endDate;
	private ComboBox<Integer> guests;
	public Button search;

	@Autowired
	public HousingSearchView(CountryService countryService, RegionService regionService, CityService cityService, HousingService housingService) {
		
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		this.housingService = housingService;
	
       // binder.setBean(this);		
	}
	
	@PostConstruct
	void init(){
        //addComponent(putMenuBar());
        putMenuBar();
	}
	
	public void putMenuBar()
	{
		//Componentes del layout
		this.offers = new CssLayout();		
		this.offers.setSizeFull();
		this.addStyleName("outlined");
		
		this.search = new Button("Search");
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		this.startDate = new DateField();
		this.endDate = new DateField();
		this.guests = new ComboBox<>(null, IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		
		//Seteo para hacer mas facil el debug
		city.setItems(cityService.findOne(700044L));
		city.setSelectedItem(cityService.findOne(700044L));
		//
		// Creamos los controles para el filtrado de los anuncios
		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");
		guests.setPlaceholder("Number of guests");
		startDate.setValue(LocalDate.now());
		startDate.setPlaceholder("Entry Date");
		startDate.setRangeStart(LocalDate.now());
		endDate.setValue(LocalDate.now().plusDays(1));
		endDate.setPlaceholder("End date");
		endDate.setRangeStart(LocalDate.now().plusDays(1));
		//searchButtons.setWidth("25%");
		
		//Dandole funcionalidad a los desplegable de 
		country.addValueChangeListener(
				event -> {
					if(!country.isEmpty())
						region.setItems(countryService.findOne(event.getValue().getId()).getRegion());
				});
		region.addValueChangeListener(
			event -> {
				if(!region.isEmpty())
					city.setItems(regionService.findOne(event.getValue().getId()).getCities());
			}
		);
		search.addClickListener(event -> getOffers());
		
        //Realizamos las validaciones
		  
        binder.forField(country).bind(s -> this._country, (s, v) -> this._country = v);
        binder.forField(region).bind(s -> this._region, (s, v) -> this._region = v);
        binder.forField(city).asRequired("Is Required").bind(s -> this._city, (s, v) -> this._city = v);
        binder.forField(startDate)
        	.withValidator(new DateRangeValidator("The entry date is previous today", LocalDate.now(), null))
        	.asRequired("Is Required")
        	.bind(s -> this._startDate, (s, v) -> this._startDate = v);
        binder.forField(endDate)
        	.withValidator(new DateRangeValidator("The end date is the same day or previous at entry Date", 
        			startDate.getValue().plusDays(1), null))
        	.asRequired("Is Required")
        	.bind(s -> this._endDate, (s, v) -> this._endDate = v);
        binder.forField(guests).asRequired("Is Required").bind(s -> this._guests, (s, v) -> this._guests = v);
        
        //mostramos el boton buscar si el binder está validado
		binder.addStatusChangeListener(event -> search.setEnabled(binder.isValid()));
        
		//Para mostrar el estado de validacion
		Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);
        
        //Hacemos el autobind con todos los atributos de esta instancia
        binder.bindInstanceFields(this);
        
        
		addComponent(new CssLayout(new VerticalLayout(country, region, city, startDate, endDate, guests, search)));
		//return 	new CssLayout(new VerticalLayout(country, region, city, startDate, endDate, guests, search));
	}
	
	public void getOfferDescription(Housing housing)
	{
        final Window window = new Window(housing.getName() + " Description page");
        VerticalLayout subContent = new VerticalLayout();
        final HousingView housingView = new HousingView(housing);
        window.setModal(true);
        window.center();
        window.setResizable(false);
        window.setSizeFull();
        window.setWidth(95, Unit.PERCENTAGE);
        window.setHeight(85, Unit.PERCENTAGE);
        
        subContent.addComponent(new HousingView(housing));
        window.setContent(subContent);

        getUI().getUI().addWindow(window);
	}
	
	public void getOffers()
	{	
		List<Housing> listaCasas = housingService.findByCityidAndAvailabilityAndGuest(city.getValue(), startDate.getValue(), endDate.getValue(), guests.getValue());

		if(listaCasas.isEmpty())
		{
			Notification.show("No se han encontrado ofertas",
	                  "Pruebe a modificar los valores del filtro",
	                  Notification.Type.HUMANIZED_MESSAGE);
			return;
		}
		Component menuBar = this.getComponent(0);
		this.removeAllComponents();
		addComponent(menuBar);

		//image.addStyleName(ValoTheme.);
		

		
		listaCasas.forEach(e -> {
			
			// Image as a file resource

			FileResource resource = null;
			ClassPathResource file = new ClassPathResource("images/foto1.jpg");
			try {
				resource = new FileResource(file.getFile());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			VerticalLayout anuncio = new VerticalLayout();
			// Show the image in the application
			Image image = new Image(null, resource);
			image.setWidth("240px");
			anuncio.setSpacing(true);
			//hay que añadir el atributo tipo
			anuncio.addComponent(image);
			anuncio.addComponent(new Label("Type: Private BedRoom · " + e.getBeds() + " Beds"));
			anuncio.addComponent(new Label(e.getName()));
			
			//Hay que calcular desde que precio se puede alquilar
			
			Float fromPrice = Float.POSITIVE_INFINITY;
			for(Availability a : e.getAvailability())
				if(a.getPrice() < fromPrice)
					fromPrice = a.getPrice();	

			anuncio.addComponent(new Label("From " + fromPrice + "€ per Night, Rating: " + e.getAssessment()));
			anuncio.setSizeFull();
			
			anuncio.addLayoutClickListener(event -> getOfferDescription(e));

			addComponent(new CssLayout(anuncio));
		}
		);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}