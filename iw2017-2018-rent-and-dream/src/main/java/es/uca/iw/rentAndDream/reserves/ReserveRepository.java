package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.housing.Housing;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

	//public List<Reserve> findByIdStartsWithIgnoreCase(Long id);
	
	public Reserve findById(Long id);
	
	@Query("select r from Reserve r")
	public List<Reserve> findByHousingBetweenDates();
}
