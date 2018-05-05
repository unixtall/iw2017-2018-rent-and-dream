package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.countries.Country;

@Entity
public class Reserve {
	//HOLA K ASE
	@Id
	@GeneratedValue
	private Long id;
	
	private Integer number_guests;
	
	private LocalDate entry_date;
	
	private LocalDate departure_date;
	
	private float price;
	
	private boolean confirmed;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public User user;
	
	/*@ManyToOne(fetch=FetchType.LAZY) 
    public Housing housing;*/
	
	protected Reserve() {
		
	}
	
	public Reserve(Integer number_guests, LocalDate entry_date, LocalDate departure_date, float price,
			boolean confirmed) {
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

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isConfirmed() {
		return confirmed;
	}
 
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
}
