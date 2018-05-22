package es.uca.iw.rentAndDream.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Reserve {
	@Id
	@GeneratedValue
	private Long id;
	
	private Integer numberGuests;
	
	private LocalDate entryDate;
	
	private LocalDate departureDate;
	
	private Float price;
	
	private ReserveTypeStatus status;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public User user;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public Housing housing;
	
	protected Reserve() {}
	
	public Reserve(Integer number_guests, LocalDate entry_date, LocalDate departure_date, Float price,
			ReserveTypeStatus status) {
		this.numberGuests = number_guests;
		this.entryDate = entry_date;
		this.departureDate = departure_date;
		this.price = price;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public Integer getNumberGuests() {
		return numberGuests;
	}

	public void setNumberGuests(Integer number_guests) {
		this.numberGuests = number_guests;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entry_date) {
		this.entryDate = entry_date;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departure_date) {
		this.departureDate = departure_date;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	
	public ReserveTypeStatus getStatus() {
		return status;
	}
 
	public void setStatus(ReserveTypeStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Housing getHousing() {
		return housing;
	}

	public void setHousing(Housing housing) {
		this.housing = housing;
	}


	@Override
	public String toString() {
		return id.toString();
	}
	
	
}
