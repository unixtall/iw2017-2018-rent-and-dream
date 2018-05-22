package es.uca.iw.rentAndDream.views;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.templates.CitySearchForm;
import es.uca.iw.rentAndDream.templates.HousingSearchResults;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	private CityService cityService;
	private HousingService housingService;
	
	private Binder<HousingSearchView> binder = new Binder<HousingSearchView>(HousingSearchView.class);
	
	private LocalDate _startDate;
	private LocalDate _endDate;
	private Integer _guests;
	private List<Housing> _results;	
	
	private CitySearchForm citySearchForm;
	private DateField startDate;
	private DateField endDate;
	private ComboBox<Integer> guests;
	public Button searchButton;
	
	//Para mostrar el estado de validacion
	Label validationStatus = new Label();
	
	private HorizontalItemLayout horizontalItemLayout;
	private VerticalLayout form;

	//Inyectamos la dependencia housingSearch
	@Autowired
	public HousingSearchView(CityService cityService, HousingService housingService) {
		super();
		this.cityService = cityService;
		this.housingService = housingService;
		
		this.citySearchForm = cityService.getCitySearchForm();
		this.startDate = new DateField();
		this.endDate = new DateField();
		this.guests = new ComboBox<>(null, IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		this.searchButton = new Button("Search");
		this._results = new ArrayList<Housing>();
				
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.form = new VerticalLayout();
	}

	@PostConstruct
	void init(){

		citySearchForm.setMargin(false);
		startDate.setValue(LocalDate.now());
		startDate.setPlaceholder("Entry Date");
		startDate.setRangeStart(LocalDate.now());
		endDate.setValue(LocalDate.now().plusDays(1));
		endDate.setPlaceholder("End date");
		endDate.setRangeStart(LocalDate.now().plusDays(1));
		guests.setPlaceholder("Number of guests");
        searchButton.setEnabled(false);
		
        form.addComponents(citySearchForm, startDate, endDate, guests, searchButton);
		
	    binder.forField(startDate)
	    	.withValidator(new DateRangeValidator("The entry date is previous today", LocalDate.now(), null))
	    	.asRequired("Is Required")
	    	.bind(s -> this._startDate, (s, v) -> this._startDate = v);
	    binder.forField(endDate)
	    	.withValidator(new DateRangeValidator("The end date is the same day or previous at entry Date", 
	    			startDate.getValue().plusDays(1), null))
	    	.asRequired("Is Required")
	    	.bind(s -> this._endDate, (s, v) -> this._endDate = v);
	    binder.forField(guests)
	    	.asRequired("Is Required")
	    	.bind(s -> this._guests, (s, v) -> this._guests = v);
		
        binder.setStatusLabel(validationStatus);
        binder.bindInstanceFields(this);
        
        binder.addStatusChangeListener(
        		event -> searchButton.setEnabled(citySearchForm.getBinder().isValid() && binder.isValid())
        );
        		
        citySearchForm.getBinder().addStatusChangeListener(
        		event -> searchButton.setEnabled(citySearchForm.getBinder().isValid() && binder.isValid())
        );

        
		this.searchButton.addClickListener(event -> 
		{
			horizontalItemLayout.removeAllComponents();
			horizontalItemLayout.addComponent(form);
			List<Housing> results = housingService.findByCityidAndAvailabilityAndGuest
					(citySearchForm.getCity().getValue(), startDate.getValue(), endDate.getValue(), guests.getValue());
			results.forEach(e -> 
				horizontalItemLayout.addComponent(new HousingSearchResults(e))
			);
		});
		
		horizontalItemLayout.addComponent(form);
		addComponent(horizontalItemLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}

/*

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Country;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Region;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.CountryService;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.RegionService;
import es.uca.iw.rentAndDream.templates.Offer;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	private CityService cityService;
	private RegionService regionService;
	private CountryService countryService;
	private HousingService housingService;
	
	private Binder<HousingSearchView> binder = new Binder<HousingSearchView>(HousingSearchView.class);
	
	private Country _country;
	private Region _region;
	private City _city;
	private LocalDate _startDate;
	private LocalDate _endDate;
	private Integer _guests;
	private List<Housing> _results;	
	
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	private DateField startDate;
	private DateField endDate;
	private ComboBox<Integer> guests;
	public Button searchButton;
	
	//Para mostrar el estado de validacion
	Label validationStatus = new Label();
	
	private HorizontalItemLayout horizontalItemLayout;
	private VerticalLayout form;

	//Inyectamos la dependencia housingSearch
	@Autowired
	public HousingSearchView(CityService cityService, RegionService regionService, CountryService countryService,
			HousingService housingService) {
		super();
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		this.housingService = housingService;
		
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		this.startDate = new DateField();
		this.endDate = new DateField();
		this.guests = new ComboBox<>(null, IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		this.searchButton = new Button("Search");
		this._results = new ArrayList<Housing>();
				
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.form = new VerticalLayout();
	}

	@PostConstruct
	void init(){
		
		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");

		startDate.setValue(LocalDate.now());
		startDate.setPlaceholder("Entry Date");
		startDate.setRangeStart(LocalDate.now());
		endDate.setValue(LocalDate.now().plusDays(1));
		endDate.setPlaceholder("End date");
		endDate.setRangeStart(LocalDate.now().plusDays(1));
		guests.setPlaceholder("Number of guests");
        searchButton.setEnabled(false);
		
        form.addComponents(country, region, city, startDate, endDate, guests, searchButton);
        
		binder.forField(country).asRequired("Is Required").bind(s -> this._country, (s, v) -> this._country = v);
		binder.forField(region).asRequired("Is Required").bind(s -> this._region, (s, v) -> this._region = v);
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
	    binder.forField(guests)
	    	.asRequired("Is Required")
	    	.bind(s -> this._guests, (s, v) -> this._guests = v);
		
        binder.setStatusLabel(validationStatus);
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(event -> searchButton.setEnabled(binder.isValid()));

        country.addValueChangeListener(
				event -> {
					if(!country.isEmpty())
						region.setItems(countryService.findOne(event.getValue().getId()).getRegion());
		});
		
		region.addValueChangeListener(
			event -> {
				if(!region.isEmpty())
					city.setItems(regionService.findOne(event.getValue().getId()).getCities());
		});
        
		this.searchButton.addClickListener(event -> 
		{
			horizontalItemLayout.removeAllComponents();
			horizontalItemLayout.addComponent(form);
			List<Housing> results = housingService.findByCityidAndAvailabilityAndGuest
					(city.getValue(), startDate.getValue(), endDate.getValue(), guests.getValue());
			results.forEach(e -> 
				horizontalItemLayout.addComponent(new Offer(e))
			);
		});
		
		horizontalItemLayout.addComponent(form);
		addComponent(horizontalItemLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}*/