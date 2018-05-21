package es.uca.iw.rentAndDream.availabilities;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.users.User;


public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

	//public List<Availability> findByIdStartsWithIgnoreCase(Long id);
	
	public Availability findById(Long id);
	
	@EntityGraph(attributePaths = {"housing"})
	public Availability findOne(Long arg);
	
	@Query("select a from Availability a "
			+ "left join fetch a.housing h "
			+ "where ?1 = h.user")
	public List<Availability> findByUser(User user);
	
	@Query("select a from Availability a "
			+ "left join fetch a.housing h "
			+ "where ?1 = h")
	public List<Availability> findByHousing(Housing housing);
	
	@Query("select a from Availability a "
			+ "left join fetch a.housing h "
			+ "where h.user = ?1 and h.name like ?2% ")
	public List<Availability> findByUserAndHousingName(User user, String housingName);
	
	
}
