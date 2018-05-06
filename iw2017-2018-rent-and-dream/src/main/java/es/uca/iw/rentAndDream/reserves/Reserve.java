package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.users.User;

@Entity
public class Reserve {

	@Id
	@GeneratedValue
	private Long id;
	
	private Integer number_guests;
	
	private LocalDate entry_date;
	
	private LocalDate departure_date;
	
	private Float price;
	
	private Boolean confirmed;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public User user;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public Housing housing;
	
	protected Reserve() {}
	
	public Reserve(Integer number_guests, LocalDate entry_date, LocalDate departure_date, Float price,
			Boolean confirmed) {
		this.number_guests = number_guests;
		this.entry_date = entry_date;
		this.departure_date = departure_date;
		this.price = price;
		this.confirmed = confirmed;
	}

	public Integer getNumber_guests() {
		return number_guests;
	}

	public void setNumber_guests(Integer number_guests) {
		this.number_guests = number_guests;
	}

	public LocalDate getEntry_date() {
		return entry_date;
	}

	public void setEntry_date(LocalDate entry_date) {
		this.entry_date = entry_date;
	}

	public LocalDate getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(LocalDate departure_date) {
		this.departure_date = departure_date;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Boolean isConfirmed() {
		return confirmed;
	}
 
	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
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

	public Boolean getConfirmed() {
		return confirmed;
	}
	
}
