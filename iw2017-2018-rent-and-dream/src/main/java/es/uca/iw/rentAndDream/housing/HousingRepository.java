package es.uca.iw.rentAndDream.housing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HousingRepository extends JpaRepository<Housing, Long> {

	public List<Housing> findByNameStartsWithIgnoreCase(String name);
	
	public Housing findByName(String name);
}
