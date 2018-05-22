package es.uca.iw.rentAndDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.rentAndDream.entities.Country;


public interface CountryRepository extends JpaRepository<Country, Long> {

	public List<Country> findByNameStartsWithIgnoreCase(String name);
	
	@EntityGraph(attributePaths = {"region"})
	public Country findOne(Long id);
	
	public Country findByName(String name);
}
