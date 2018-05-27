package es.uca.iw.rentAndDream.services;

import java.time.LocalDate;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.HousingRepository;

@Service
public class HousingService {

	@Autowired
	private HousingRepository repo;
	
	@Autowired
	private CityService cityService;
	
	
	/*@Autowired
	private HousingEditor housingEditor;
	
	@Autowired
	private AvailabilityEditor availabilityEditor;*/
	

	public Housing findByName(String name) throws NameNotFoundException {

		Housing housing = repo.findByName(name);
		if (housing == null) {
			throw new NameNotFoundException(name);
		}
		return housing;
	}

	public Housing save(Housing housing) {
		return repo.save(housing);
	}
	

	public List<Housing> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}
	
	public Housing findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public Housing findOneWithAvailabilityAndCity(Long id)
	{
		return repo.findOneWithAvailabilityAndCity(id);
	}
	

	public void delete(Housing arg0) {
		repo.delete(arg0);
	}
	@EntityGraph(attributePaths = {"user"})
	public List<Housing> findAll() {
		return repo.findAll();
	}
	
	public List<Housing> findByCityidAndAvailabilityAndGuest(City city, LocalDate startDate, LocalDate endDate, Integer guests)
	{
		return repo.findByCityidAndAvailabilityAndGuest(city, startDate, endDate, guests);
		
	}
	

	public List<Housing> findByUser(User user)
	{
		List<Housing> housing = repo.findByUser(user);
		housing.forEach(e ->{
			e.getReserve();
			e.getAvailability();
			
		});
		return housing;
	}
	
	public Float getPrice(LocalDate entryDate, Housing housing)
	{
		return repo.getPrice(entryDate, housing);
	}
	
	public Boolean isReserved(LocalDate date, Housing housing)
	{
		return repo.isReserved(date, housing);
	}
	
}