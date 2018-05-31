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

	
	@EntityGraph(attributePaths = {"city", "user"})
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
	
	@EntityGraph(attributePaths = {"reserve", "city", "user"})
	@Query("select distinct h from Housing h "
			+ "where h.user = ?1")
	List<Housing> findByUser(User user);
	
	@EntityGraph(attributePaths = {"user", "city"})
	public Housing findOne(Long id);
	
	@Query("select h from Housing h join fetch h.city join fetch h.availability join fetch h.user a where h.id = ?1 ")
	public Housing findOneWithAvailabilityAndCityAndUser(Long id);
	
	@Query("Select a.price from Housing h, Availability a "
			+ "where h = ?2 and a.housing = h and ?1 between a.startDate and a.endDate")
	public Float getPrice(LocalDate entryDate, Housing housing);
	
	@Query("select case when (count(h) > 0) then true else false end from Availability a, Housing h, Reserve r "
			+ "where h = ?2 and a.housing = h and r.housing = h and ?1 >= r.entryDate and ?1 < r.departureDate "
			+ "and r.status = 0")
	public Boolean isReserved(LocalDate date, Housing housing);
	
	@Query("select case when (count(h) > 0) then true else false end from Availability a, Housing h, Reserve r "
			+ "where h = ?2 and a.housing = h and r.housing = h and ?1 >= r.entryDate and ?1 < r.departureDate "
			+ "and (r.status = 0 or (r.status = 1 and r.user = ?3))")
	public Boolean isReservedWithUserPendingReserve(LocalDate date, Housing housing, User user);
	
	
	@Query("select co.vat from Housing h, City c, Country co Where c = h.city and c.country = co and h = ?1 ")
	public Float getVat(Housing h);
	
	@Query("select sum(r.guestRating) from Housing h, Reserve r where r.housing = h and h = ?1 and r.status = 5 and r.guestRating <> 0")
	public Double getAssessmentSum(Housing h);
}
