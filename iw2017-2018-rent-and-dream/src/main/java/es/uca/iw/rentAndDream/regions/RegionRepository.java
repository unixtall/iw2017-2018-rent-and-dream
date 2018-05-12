package es.uca.iw.rentAndDream.regions;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.countries.Country;

public interface RegionRepository extends JpaRepository<Region, Long> {

	public List<Region> findByNameStartsWithIgnoreCase(String name);
	
	@EntityGraph(attributePaths = {"cities"})
	public Region findOne(Long id);
	
	public Region findByName(String name);
}
