package es.uca.iw.rentAndDream.components;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Country;
import es.uca.iw.rentAndDream.entities.Region;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.CountryService;
import es.uca.iw.rentAndDream.services.RegionService;


@SpringComponent
@Scope("prototype")
public class CitySearchForm extends VerticalLayout {
	
	private CityService cityService;
	private RegionService regionService;
	private CountryService countryService;
	
	private Binder<CitySearchForm> binder = new Binder<CitySearchForm>(CitySearchForm.class);
		
	private Country _country;
	private Region _region;
	private City _city;
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	
	@Autowired
	public CitySearchForm(CityService cityService, RegionService regionService, CountryService countryService)
	{
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		
		this.country = new ComboBox<Country>();
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		
		Label validationStatus = new Label();

		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");
		
	    binder.forField(country).bind(s -> this._country, (s, v) -> this._country = v);
		binder.forField(region).bind(s -> this._region, (s, v) -> this._region = v);
		binder.forField(city).asRequired("Is Required").bind(s -> this._city, (s, v) -> this._city = v);
        binder.setStatusLabel(validationStatus);
		
        binder.setBean(this);
		binder.bindInstanceFields(this);

		
		getCountry().setItems(countryService.findAll());
		
		getCountry().addValueChangeListener(
				event -> {
					if(!getCountry().isEmpty())
						getRegion().setItems(countryService.findOne(event.getValue().getId()).getRegion());	
				});
		
		getRegion().addValueChangeListener(
			event -> {
				if(!getRegion().isEmpty())
					getCity().setItems(regionService.findOne(event.getValue().getId()).getCities());
			});
		
		
		addComponents(country, region, city);
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

	public Binder<CitySearchForm> getBinder() {
		return binder;
	}

	public void setBinder(Binder<CitySearchForm> binder) {
		this.binder = binder;
	}
	
	
}