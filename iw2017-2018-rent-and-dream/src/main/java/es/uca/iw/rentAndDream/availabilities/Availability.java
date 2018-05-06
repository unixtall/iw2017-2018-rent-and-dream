package es.uca.iw.rentAndDream.availabilities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.uca.iw.rentAndDream.housing.Housing;

@Entity
public class Availability {

	@Id
	@GeneratedValue
	private Long id;
	
	private LocalDate start_date;
	
	private LocalDate end_date;
	
	private Double price;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	private Housing housing;
	
	
	public Availability(Housing housing, Long id, LocalDate start_date, LocalDate end_date, Double price) {
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

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}
