package es.uca.iw.rentAndDream.cities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CityRepository extends JpaRepository<City, Long> {

	public List<City> findByNameStartsWithIgnoreCase(String name);
	
	public City findByName(String name);
}
