package es.uca.iw.rentAndDream.services;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.AvailabilityRepository;

@Service
public class AvailabilityService {

	@Autowired
	private AvailabilityRepository availabilityRepo;
	
	@Autowired
	private HousingService housingService;

	public Availability loadAvailabilityById(Long id) throws NameNotFoundException {

		Availability availability = availabilityRepo.findById(id);
		if (availability == null) {
			throw new NameNotFoundException();
		}
		return availability;
	}

	public Availability save(Availability availability) {
		return availabilityRepo.save(availability);
	}

	/*public List<Availability> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}*/

	public Availability findOne(Long arg0) {
		return availabilityRepo.findOne(arg0);
	}

	public void delete(Availability arg0) {
		availabilityRepo.delete(arg0);
	}

	public List<Availability> findAll() {
		return availabilityRepo.findAll();
	}
	
	public List<Availability> findAllWithHousing()
	{
		return availabilityRepo.findAllWithHousing();
	}
	
	public List<Availability> findByUser(User user){
		return availabilityRepo.findByUser(user);
	}
	
	public List<Availability> findByHousing(Housing housing)
	{
		return availabilityRepo.findByHousing(housing);
	}
	
	public List<Availability> findByUserAndHousingName(User user, String housingName)
	{
		return availabilityRepo.findByUserAndHousingName(user, housingName);
	}
	
	
	
	public List<Availability> findByHousingNameStartsWithIgnoreCaseWithHousing(String name){
		return availabilityRepo.findByHousingNameStartsWithIgnoreCaseWithHousing(name);
	}
}