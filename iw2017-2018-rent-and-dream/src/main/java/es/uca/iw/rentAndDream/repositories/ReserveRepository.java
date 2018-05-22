package es.uca.iw.rentAndDream.repositories;

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
	
	
}
