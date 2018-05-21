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
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Float price;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	private Housing housing;
	
	public Availability(LocalDate startDate, LocalDate endDate, Float price, Housing housing) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.housing = housing;
	}
	public Availability() {}
	public Housing getHousing() {
		return housing;
	}

	public void setHousing(Housing housing) {
		this.housing = housing;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	public Availability(Long id, LocalDate startDate, LocalDate endDate, Float price, Housing housing) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.housing = housing;
	}
	
	
}
