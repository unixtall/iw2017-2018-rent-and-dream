package es.uca.iw.rentAndDream.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

	//public List<Reserve> findByIdStartsWithIgnoreCase(Long id);
	
	public Reserve findById(Long id);
	
	@Query("select r from Reserve r")
	public List<Reserve> findByHousingBetweenDates();
	
	@Query("select DISTINCT h from Housing h "
			+ "JOIN FETCH h.reserve r "
			+ "where h.user = ?1 and r.status = ?2")
			
	List<Housing> findByUserAndStatus(User user, ReserveTypeStatus status);

	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h "
			+ "JOIN FETCH h.user hu "
			+ "JOIN FETCH r.user "
			+ "where h.name like %?1% and hu = ?2")
	
	public List<Reserve> findByHousingNameAndAsHost(String housing, User user);
	
	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h "
			+ "JOIN FETCH r.user u "
			+ "JOIN FETCH h.user uh "
			+ "where uh = ?1")
	public List<Reserve> findAsHost(User user);
	
	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h "
			+ "JOIN FETCH r.user u "
			+ "where u = ?1")
	public List<Reserve> findByUser(User user);
	
	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h "
			+ "JOIN FETCH r.user u "
			+ "where u.username like %?1%")
	public List<Reserve> findByGuestUsername(String userName);
	
	
	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h "
			+ "JOIN FETCH r.user u")
	public List<Reserve> findAllWithHousingAndUser();
	
	@Query("Select r from Reserve r "
			+ "JOIN FETCH r.housing h where h = ?1 and r.entryDate >= ?2")
	public List<Reserve> findByHousing(Housing housing, LocalDate entryDate);
	
	
}
