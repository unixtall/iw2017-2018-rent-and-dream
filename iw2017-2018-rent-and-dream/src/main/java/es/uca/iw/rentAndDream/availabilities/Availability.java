package es.uca.iw.rentAndDream.availabilities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Availability {

	@ManyToOne(fetch=FetchType.LAZY) 
	private Housing housing;
	@Id
	@GeneratedValue
	private Long id;

	
	private LocalDate start_date;

	private double end_date;
	private double price;
	
	public Availability(Housing housing, Long id, LocalDate start_date, double end_date, double price) {
		this.housing = housing;
		this.id = id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.price = price;
	}

	public Housing getHousing() {
		return housing;
	}

	public void setHousing(Housing housing) {
		this.housing = housing;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public double getEnd_date() {
		return end_date;
	}

	public void setEnd_date(double end_date) {
		this.end_date = end_date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
	
	
	
}
