package es.uca.iw.rentAndDream.services;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.repositories.CityRepository;
import es.uca.iw.rentAndDream.repositories.CountryRepository;
import es.uca.iw.rentAndDream.repositories.RegionRepository;

@Service
public class CityService {

	@Autowired
	private CountryService countryService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private CityRepository cityRepo;

	public City loadCityByName(String name) throws NameNotFoundException {

		City city = cityRepo.findByName(name);
		if (city == null) {
			throw new NameNotFoundException(name);
		}
		return city;
	}

	public City save(City city) {
		return cityRepo.save(city);
	}

	public List<City> findByNameStartsWithIgnoreCase(String name) {
		return cityRepo.findByNameStartsWithIgnoreCase(name);
	}

	public City findOne(Long arg0) {
		return cityRepo.findOne(arg0);
	}

	public void delete(City arg0) {
		cityRepo.delete(arg0);
	}

	public List<City> findAll() {
		return cityRepo.findAll();
	}

	/*
	public CitySearchForm getCitySearchForm()
	{
		CitySearchForm citySearchForm = new CitySearchForm();
		
		citySearchForm.getCountry().setItems(countryService.findAll());
		
		citySearchForm.getCountry().addValueChangeListener(
				event -> {
					if(!citySearchForm.getCountry().isEmpty())
						citySearchForm.getRegion().setItems(countryService.findOne(event.getValue().getId()).getRegion());	
				});
		
		citySearchForm.getRegion().addValueChangeListener(
			event -> {
				if(!citySearchForm.getRegion().isEmpty())
					citySearchForm.getCity().setItems(regionService.findOne(event.getValue().getId()).getCities());
			});
		
		return citySearchForm;
	}*/
	
}