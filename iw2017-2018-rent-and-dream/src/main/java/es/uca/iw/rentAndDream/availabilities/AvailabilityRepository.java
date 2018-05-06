package es.uca.iw.rentAndDream.availabilities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

	public List<Availability> findByNameStartsWithIgnoreCase(String name);
	
	public Availability findByName(String name);
}
