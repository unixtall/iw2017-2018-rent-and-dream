package es.uca.iw.rentAndDream.countries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, Long> {

	public List<Country> findByNameStartsWithIgnoreCase(String name);
	
	public Country findByName(String name);
}
