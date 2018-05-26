package es.uca.iw.rentAndDream.services;

import java.time.LocalDate;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.ReserveRepository;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository repo;
	
	@Autowired
	private HousingService housingService;
	
	@Autowired
	private UserService userService;

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
	
	public List<Reserve> findByHousingNameAndAsHost(String housing, User user)
	{
		return repo.findByHousingNameAndAsHost(housing, user);
	}
	

	public List<Reserve> findAsHost(User user)
	{
		return repo.findAsHost(user);
	}
	
	public List<Reserve> findByUser(User user)
	{
		return repo.findByUser(user);
	}
	
	public List<Reserve> findByGuestUsername(String userName)
	{
		return repo.findByGuestUsername(userName);
	}
	
	public Reserve findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Reserve arg0) {
		repo.delete(arg0);
	}

	public List<Reserve> findAll() {
		return repo.findAll();
	}
	
	public List<Housing> findByUserAndStatus(User user, ReserveTypeStatus status){
		return repo.findByUserAndStatus(user, status);
	}
	
	public List<Reserve> findAllWithHousingAndUser(){
		return repo.findAllWithHousingAndUser();
	}
	
	public List<Reserve> findByHousing(Housing housing, LocalDate entryDate){
		return repo.findByHousing(housing, entryDate);
	}
	
}
