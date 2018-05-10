package es.uca.iw.rentAndDream.regions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, Long> {

	public List<Region> findByNameStartsWithIgnoreCase(String name);
	
	public Region findByName(String name);
}
