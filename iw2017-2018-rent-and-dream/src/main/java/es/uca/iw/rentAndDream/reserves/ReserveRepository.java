package es.uca.iw.rentAndDream.reserves;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

	//public List<Reserve> findByIdStartsWithIgnoreCase(Long id);
	
	public Reserve findById(Long id);
}
