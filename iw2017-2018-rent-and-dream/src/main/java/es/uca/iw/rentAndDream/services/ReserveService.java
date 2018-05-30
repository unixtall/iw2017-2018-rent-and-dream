package es.uca.iw.rentAndDream.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.Transaction;
import es.uca.iw.rentAndDream.entities.TransactionType;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.ReserveRepository;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository reserveRepo;
	
	@Autowired
	private HousingService housingService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private TransactionService transactionService;
	
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
		
		reserve.setPrice(calculatePrice(reserve.getEntryDate(), reserve.getDepartureDate(), reserve.getHousing()) * (1 + housingService.getVat(reserve.getHousing())));
	
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
	
	public List<Reserve> findByGuestUsernameByHousingName(String userName, String string)
	{
		return reserveRepo.findByGuestUsernameByHousingName(userName, string);
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
		
		return sum ;
	}
	
	@Transactional
	public void confirm(Reserve reserve)
	{
		User host = userService.findByHousing(reserve.getHousing());
		City city = cityService.findByHousing(reserve.getHousing());
		
		Transaction transaction = new Transaction(host, reserve.getUser(), reserve);
		transaction.setDateTime(LocalDateTime.now());
		transaction.setType(TransactionType.GUESTTOHOST);
		transaction.setAmount(reserve.getPrice() - reserve.getPrice() * 0.05f);
		transaction.setTransactionProfit(reserve.getPrice() * 0.05f);
		transaction.setInvoice("Your Reservation with id: " + reserve.getId() + " has been accepted by "
				+ host.getFirstName() + " " + host.getLastName() + " (" + host.getUsername() + ")" 
				+ " at " + transaction.getDateTime() + "\r\n" + 
				"\r\n" + 
				"Here is a summary of your reservation:\r\n" + 
				"Name of the house: " + reserve.getHousing() + "\r\n" + 
				"City: " + city.getName()  + "\r\n" + 
				" Region: " + city.getRegion().getName()  + "\r\n" + 
				" Country: " + city.getCountry().getName() + "\r\n" +
				"Address: " + reserve.getHousing().getAddress() + "\r\n" + 
				"Entry date: " + reserve.getEntryDate() + " \r\n" + 
				"Departure date: " + reserve.getDepartureDate() + " \r\n" + 
				"Price: " + reserve.getPrice() + " vat incluyed");
		
		reserve.setStatus(ReserveTypeStatus.CONFIRMED);
		
		reserveRepo.save(reserve);
		transactionService.save(transaction);
	}
	
	
	@Transactional
	public void cancel(Reserve reserve)
	{
		User host = userService.findByHousing(reserve.getHousing());
		City city = cityService.findByHousing(reserve.getHousing());
		
		Transaction transaction = new Transaction(host, reserve.getUser(), reserve);
		transaction.setDateTime(LocalDateTime.now());
		transaction.setType(TransactionType.HOSTTOGUEST);
		transaction.setAmount(getAmountRefundCancellationPolicy(reserve));
		transaction.setInvoice("Your Reservation with id: " + reserve.getId() + " has cancelled"
				+ " at " + transaction.getDateTime() + "\r\n" + 
				"\r\n" + 
				transaction.getAmount() + " will be refunded:\r\n");
		
		reserve.setStatus(ReserveTypeStatus.CONFIRMED);
		
		reserveRepo.save(reserve);
		transactionService.save(transaction);
	}
	
	public Float getAmountRefundCancellationPolicy(Reserve reserve)
	{
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime reserveDateTime = LocalDateTime.of(reserve.getEntryDate(), LocalTime.of(12, 00));
			
		//Si se cancela una reserva entre 48 y 24 horas hay una penalización de 60%
		if(nowDateTime.isBefore(reserveDateTime.minusHours(24)) && nowDateTime.isAfter(reserveDateTime.minusHours(48)))
			return reserve.getPrice() * 0.4F;
		else
		//Si se cancela una reserva dentro de las 24 horas antes hay una penalización de 100%
		if(nowDateTime.isAfter(reserveDateTime.minusHours(24)))
			return 0F;
		
		//Si se cancela una reserva con una anterioridad de superior a 48 no hay penalizacion
		return reserve.getPrice();
	}
}
