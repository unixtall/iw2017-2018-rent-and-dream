package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.users.User;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository repo;

	public Reserve loadReserveById(Long id) throws NameNotFoundException {

		Reserve reserve = repo.findById(id);
		if (reserve == null) {
			throw new NameNotFoundException(id.toString());
		}
		return reserve;
	}

	public Reserve save(Reserve reserve) {
		return repo.save(reserve);
	}

	/*
	public List<Reserve> findByIdStartsWithIgnoreCase(Long id) {
		return repo.findByIdStartsWithIgnoreCase(id);
	}
*/
	public Reserve findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Reserve arg0) {
		repo.delete(arg0);
	}

	public List<Reserve> findAll() {
		return repo.findAll();
	}
	
	public List<Housing> findByUserAndStatus(User user, TypeReserveStatus status){
		return repo.findByUserAndStatus(user, status);
	}
}
