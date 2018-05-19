package es.uca.iw.rentAndDream.housing;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.cities.City;

public interface HousingRepository extends JpaRepository<Housing, Long> {

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
}
