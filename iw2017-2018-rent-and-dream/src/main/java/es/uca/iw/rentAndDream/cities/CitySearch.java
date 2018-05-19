package es.uca.iw.rentAndDream.cities;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.countries.Country;
import es.uca.iw.rentAndDream.countries.CountryService;
import es.uca.iw.rentAndDream.housing.HousingSearch;
import es.uca.iw.rentAndDream.regions.Region;
import es.uca.iw.rentAndDream.regions.RegionService;

@SpringComponent
@UIScope
public class CitySearch extends VerticalLayout {

	private Binder<CitySearch> binder = new Binder<CitySearch>(CitySearch.class);
	private final CityService cityService;
	private final RegionService regionService;
	private final CountryService countryService;
	private Country _country;
	private Region _region;
	private City _city;
	private ComboBox<Country> country; 
	private ComboBox<Region> region; 
	private ComboBox<City> city; 
	
	@Autowired
	public CitySearch(CityService cityService, RegionService regionService, CountryService countryService) throws ValidationException {
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;

		Label validationStatus = new Label();
		
		this.country = new ComboBox<Country>(null, countryService.findAll());
		this.region = new ComboBox<Region>();
		this.city = new ComboBox<City>();
		
		//Seteo para hacer mas facil el debug (BORRAR)
		country.setItems(countryService.findOne(62L));
		country.setSelectedItem(countryService.findOne(62L));
		region.setItems(regionService.findOne(965L));
		region.setSelectedItem(regionService.findOne(965L));
		city.setItems(cityService.findOne(700044L));
		city.setSelectedItem(cityService.findOne(700044L));
		
		//Finseteo
		
		//
		// Creamos los controles para el filtrado de los anuncios
		country.setPlaceholder("Select a Country");
		region.setPlaceholder("Select a Region");
		city.setPlaceholder("Select a City");
		
	    binder.forField(country).asRequired("Is Required").bind(s -> this._country, (s, v) -> this._country = v);
		binder.forField(region).asRequired("Is Required").bind(s -> this._region, (s, v) -> this._region = v);
		binder.forField(city).asRequired("Is Required").bind(s -> this._city, (s, v) -> this._city = v);
        binder.setStatusLabel(validationStatus);
		
		binder.bindInstanceFields(this);
		binder.writeBean(this);
		
		country.addValueChangeListener(
				event -> {
					if(!country.isEmpty())
						region.setItems(countryService.findOne(event.getValue().getId()).getRegion());
					try {
						binder.writeBean(this);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		
		region.addValueChangeListener(
			event -> {
				if(!region.isEmpty())
					city.setItems(regionService.findOne(event.getValue().getId()).getCities());
				try {
					binder.writeBean(this);
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	public interface ChangeHandler {
		void onChange();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
	}
}