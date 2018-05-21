package es.uca.iw.rentAndDream.availabilities;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.users.User;

@Service
public class AvailabilityService {

	@Autowired
	private AvailabilityRepository repo;

	public Availability loadAvailabilityById(Long id) throws NameNotFoundException {

		Availability availability = repo.findById(id);
		if (availability == null) {
			throw new NameNotFoundException();
		}
		return availability;
	}

	public Availability save(Availability availability) {
		return repo.save(availability);
	}

	/*public List<Availability> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}*/

	public Availability findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Availability arg0) {
		repo.delete(arg0);
	}

	public List<Availability> findAll() {
		return repo.findAll();
	}
	
	public List<Availability> findByUser(User user){
		return repo.findByUser(user);
	}
	
	public List<Availability> findByHousing(Housing housing)
	{
		return repo.findByHousing(housing);
	}
	
	public List<Availability> findByUserAndHousingName(User user, String housingName)
	{
		return repo.findByUserAndHousingName(user, housingName);
	}
}