package es.uca.iw.rentAndDream.countries;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, Long> {

	public List<Country> findByNameStartsWithIgnoreCase(String name);
	
	@EntityGraph(attributePaths = {"region"})
	public Country findOne(Long id);
	
	public Country findByName(String name);
}
