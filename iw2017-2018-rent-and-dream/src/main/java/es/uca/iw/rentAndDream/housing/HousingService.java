package es.uca.iw.rentAndDream.housing;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HousingService {

	@Autowired
	private HousingRepository repo;

	public Housing loadHousingByName(String name) throws NameNotFoundException {

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

	public void delete(Housing arg0) {
		repo.delete(arg0);
	}

	public List<Housing> findAll() {
		return repo.findAll();
	}
}