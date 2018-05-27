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
import es.uca.iw.rentAndDream.repositories.HousingRepository;
import es.uca.iw.rentAndDream.repositories.ReserveRepository;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository reserveRepo;
	
	@Autowired
	private HousingService housingService;
	
	@Autowired
	private UserService userService;

	public Reserve loadReserveById(Long id) throws NameNotFoundException {

		Reserve reserve = reserveRepo.findById(id);
		if (reserve == null) {
			throw new NameNotFoundException(id.toString());
		}
		return reserve;
	}

	public Reserve save(Reserve reserve) {
		
		reserve.setPrice(calculatePrice(reserve.getEntryDate(), reserve.getDepartureDate(), reserve.getHousing()));
	
		return reserveRepo.save(reserve);
	}

	/*
	public List<Reserve> findByIdStartsWithIgnoreCase(Long id) {
		return repo.findByIdStartsWithIgnoreCase(id);
	}
	
*/
	
	public List<Reserve> findByHousingNameAndAsHost(String housing, User user)
	{
		return reserveRepo.findByHousingNameAndAsHost(housing, user);
	}
	

	public List<Reserve> findAsHost(User user)
	{
		return reserveRepo.findAsHost(user);
	}
	
	public List<Reserve> findByUser(User user)
	{
		return reserveRepo.findByUser(user);
	}
	
	public List<Reserve> findByGuestUsername(String userName)
	{
		return reserveRepo.findByGuestUsername(userName);
	}
	
	public Reserve findOne(Long arg0) {
		return reserveRepo.findOne(arg0);
	}

	public void delete(Reserve arg0) {
		reserveRepo.delete(arg0);
	}

	public List<Reserve> findAll() {
		return reserveRepo.findAll();
	}
	
	public List<Housing> findByUserAndStatus(User user, ReserveTypeStatus status){
		return reserveRepo.findByUserAndStatus(user, status);
	}
	
	public List<Reserve> findAllWithHousingAndUser(){
		return reserveRepo.findAllWithHousingAndUser();
	}
	
	public List<Reserve> findByHousing(Housing housing, LocalDate entryDate){
		return reserveRepo.findByHousing(housing, entryDate);
	}
	
	public Float calculatePrice(LocalDate entryDate, LocalDate departureDate, Housing housing)
	{
		Float sum = 0f;
		
		for(LocalDate d = entryDate; d.isBefore(departureDate);  d = d.plusDays(1))
		{
			sum += housingService.getPrice(d, housing);
		}
		
		return sum;
	}
}
