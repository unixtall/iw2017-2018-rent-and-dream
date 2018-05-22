package es.uca.iw.rentAndDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Country;
import es.uca.iw.rentAndDream.entities.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

	public List<Region> findByNameStartsWithIgnoreCase(String name);
	
	@EntityGraph(attributePaths = {"cities"})
	public Region findOne(Long id);
	
	public Region findByName(String name);
}
