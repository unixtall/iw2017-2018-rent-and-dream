package es.uca.iw.rentAndDream.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;

public interface HousingRepository extends JpaRepository<Housing, Long> {

	
	@EntityGraph(attributePaths = {"city"})
	public List<Housing> findAll();
	
	@EntityGraph(attributePaths = {"city"})
	public List<Housing> findByNameStartsWithIgnoreCase(String name);
	
	public Housing findByName(String name);

	//@EntityGraph(attributePaths = {"city", "availability", "reserve"})
	@Query("select DISTINCT h from Housing h "
			+ "LEFT JOIN FETCH h.availability a "
			+ "LEFT JOIN FETCH h.city c "
			//+ "LEFT JOIN FETCH h.reserve r "
			+ "where h.city = ?1 and h.beds >= ?4 and ?2 < a.endDate and ?3 >= a.startDate ")
	List<Housing> findByCityidAndAvailabilityAndGuest(City city, LocalDate startDate, 
			LocalDate endDate, Integer guests);
	
	@EntityGraph(attributePaths = {"reserve", "city"})
	@Query("select distinct h from Housing h "
			+ "where h.user = ?1")
	List<Housing> findByUser(User user);
	
	@EntityGraph(attributePaths = {"city"})
	public Housing findOne(Long id);
}
