package es.uca.iw.rentAndDream.cities;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

	@Autowired
	private CityRepository repo;

	public City loadCityByName(String name) throws NameNotFoundException {

		City city = repo.findByName(name);
		if (city == null) {
			throw new NameNotFoundException(name);
		}
		return city;
	}

	public City save(City city) {
		return repo.save(city);
	}

	public List<City> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}

	public City findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(City arg0) {
		repo.delete(arg0);
	}

	public List<City> findAll() {
		return repo.findAll();
	}
}