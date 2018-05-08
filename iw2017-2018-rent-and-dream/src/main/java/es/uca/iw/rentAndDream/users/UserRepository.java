package es.uca.iw.rentAndDream.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.housing.Housing;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByLastNameStartsWithIgnoreCase(String lastName);
	
	public User findByUsername(String username);
	
	@Query("SELECT u FROM User u JOIN FETCH u.housing WHERE u.id = ?1")
	public User findByUserIdWithJoinFetch(Long id);
}

