package es.uca.iw.rentAndDream.cities;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.rentAndDream.regions.Region;


public interface CityRepository extends JpaRepository<City, Long> {

	public List<City> findByNameStartsWithIgnoreCase(String name);
	
	@EntityGraph(attributePaths = {"housing"})
	public City findByName(String name);
	
	@EntityGraph(attributePaths = {"housing"})
	public City findOne(Long id);
}
