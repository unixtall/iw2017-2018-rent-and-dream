package es.uca.iw.rentAndDream.countries;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CountryService {
		@Autowired
		private CountryRepository repo;

		public Country loadCountryByName(String name) throws NameNotFoundException {
 
			Country country = repo.findByName(name);
			if (country == null) {
				throw new NameNotFoundException(name);
			}
			return country;
		}

		public Country save(Country country) {
			return repo.save(country);
		}

		public List<Country> findByNameStartsWithIgnoreCase(String name) {
			return repo.findByNameStartsWithIgnoreCase(name);
		}

		public Country findOne(Long arg0) {
			return repo.findOne(arg0);
		}

		public void delete(Country arg0) {
			repo.delete(arg0);
		}

		public List<Country> findAll() {
			return repo.findAll();
		}
}

