package es.uca.iw.rentAndDream.availabilities;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

	@Autowired
	private AvailabilityRepository repo;

	public Availability loadAvailabilityByName(String name) throws NameNotFoundException {

		Availability availability = repo.findByName(name);
		if (availability == null) {
			throw new NameNotFoundException(name);
		}
		return availability;
	}

	public Availability save(Availability availability) {
		return repo.save(availability);
	}

	public List<Availability> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}

	public Availability findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Availability arg0) {
		repo.delete(arg0);
	}

	public List<Availability> findAll() {
		return repo.findAll();
	}
}