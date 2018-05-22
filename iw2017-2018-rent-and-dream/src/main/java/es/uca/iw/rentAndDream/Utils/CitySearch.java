package es.uca.iw.rentAndDream.Utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Country;
import es.uca.iw.rentAndDream.entities.Region;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.CountryService;
import es.uca.iw.rentAndDream.services.RegionService;

public class CitySearch extends VerticalLayout {

	private Binder<CitySearch> binder = new Binder<CitySearch>(CitySearch.class);
		
	private Country _country;
	private Region _region;
	private City _city;
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	
	public CitySearch(CityService cityService, RegionService regionService, CountryService countryService)
	{
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		
		Label validationStatus = new Label();

		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");
		
	    binder.forField(country).asRequired("Is Required").bind(s -> this._country, (s, v) -> this._country = v);
		binder.forField(region).asRequired("Is Required").bind(s -> this._region, (s, v) -> this._region = v);
		binder.forField(city).asRequired("Is Required").bind(s -> this._city, (s, v) -> this._city = v);
        binder.setStatusLabel(validationStatus);
		
		binder.bindInstanceFields(this);
		binder.setBean(this);
	
		
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
		
		addComponents(country, region, city);
		this.setMargin(false);
	}
	
	@PostConstruct
	public void init() 
	{
		//this.cityService = cityService;
		//this.regionService = regionService;
		//this.countryService = countryService;

		
	}
	
	public Country get_country() {
		return _country;
	}

	public void set_country(Country _country) {
		this._country = _country;
	}

	public Region get_region() {
		return _region;
	}

	public void set_region(Region _region) {
		this._region = _region;
	}

	public City get_city() {
		return _city;
	}

	public void set_city(City _city) {
		this._city = _city;
	}
	
	public ComboBox<Country> getCountry() {
		return country;
	}


	public void setCountry(ComboBox<Country> country) {
		this.country = country;
	}


	public ComboBox<Region> getRegion() {
		return region;
	}


	public void setRegion(ComboBox<Region> region) {
		this.region = region;
	}


	public ComboBox<City> getCity() {
		return city;
	}


	public void setCity(ComboBox<City> city) {
		this.city = city;
	}


	public Boolean isValid()
	{
		return binder.isValid();
	}
	
	
	public interface ChangeHandler {
		void onChange();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		country.addValueChangeListener(e -> h.onChange());
		region.addValueChangeListener(e -> h.onChange());
		city.addValueChangeListener(e -> h.onChange());
	}
}


/*package es.uca.iw.rentAndDream.Utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.cities.CityService;
import es.uca.iw.rentAndDream.countries.Country;
import es.uca.iw.rentAndDream.countries.CountryService;
import es.uca.iw.rentAndDream.regions.Region;
import es.uca.iw.rentAndDream.regions.RegionService;

@Service
public class CitySearch extends VerticalLayout {

	private Binder<CitySearch> binder = new Binder<CitySearch>(CitySearch.class);
	@Autowired
	private CityService cityService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private CountryService countryService;
	
	private Country _country;
	private Region _region;
	private City _city;
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	
	@PostConstruct
	public void init() 
	{
		//this.cityService = cityService;
		//this.regionService = regionService;
		//this.countryService = countryService;

		Label validationStatus = new Label();
		
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();

		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");
		
	    binder.forField(country).asRequired("Is Required").bind(s -> this._country, (s, v) -> this._country = v);
		binder.forField(region).asRequired("Is Required").bind(s -> this._region, (s, v) -> this._region = v);
		binder.forField(city).asRequired("Is Required").bind(s -> this._city, (s, v) -> this._city = v);
        binder.setStatusLabel(validationStatus);
		
		binder.bindInstanceFields(this);
		binder.setBean(this);
	
		
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
		
		addComponents(country, region, city);
		this.setMargin(false);
	}
	
	public Country get_country() {
		return _country;
	}

	public void set_country(Country _country) {
		this._country = _country;
	}

	public Region get_region() {
		return _region;
	}

	public void set_region(Region _region) {
		this._region = _region;
	}

	public City get_city() {
		return _city;
	}

	public void set_city(City _city) {
		this._city = _city;
	}
	
	public ComboBox<Country> getCountry() {
		return country;
	}


	public void setCountry(ComboBox<Country> country) {
		this.country = country;
	}


	public ComboBox<Region> getRegion() {
		return region;
	}


	public void setRegion(ComboBox<Region> region) {
		this.region = region;
	}


	public ComboBox<City> getCity() {
		return city;
	}


	public void setCity(ComboBox<City> city) {
		this.city = city;
	}


	public Boolean isValid()
	{
		return binder.isValid();
	}
	
	
	public interface ChangeHandler {
		void onChange();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		country.addValueChangeListener(e -> h.onChange());
		region.addValueChangeListener(e -> h.onChange());
		city.addValueChangeListener(e -> h.onChange());
	}
}*/