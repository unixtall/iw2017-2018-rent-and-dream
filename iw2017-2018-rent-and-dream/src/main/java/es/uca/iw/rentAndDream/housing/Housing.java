package es.uca.iw.rentAndDream.housing;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.uca.iw.rentAndDream.availabilities.Availability;
import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.reserves.Reserve;
import es.uca.iw.rentAndDream.users.User;

@Entity
public class Housing {

	@Id
	@GeneratedValue
	private Long id;	
	private String name;	
	private Float assessment;	
	private String description;	
	private Integer bedrooms;	
	private Integer beds;	
	private Boolean airConditioner;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="housing")
    private List<Availability> availability;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="housing")
    private List<Reserve> reserve;
    @ManyToOne(fetch=FetchType.LAZY)
	private User user;    
    @ManyToOne(fetch=FetchType.LAZY)
    private City city;
	
	protected Housing(){}
	
	public Housing(String name, Float assessment, String description,
			Integer bedrooms, Integer beds, Boolean airConditioner) {
		this.name = name;
		this.assessment = assessment;
		this.description = description;
		this.bedrooms = bedrooms;
		this.beds = beds;
		this.airConditioner = airConditioner;
	}

	public Long getId() {
		return id;
	}

	public User getIdUser() {
		return user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public Float getAssessment() {
		return assessment;
	}

	public void setAssessment(Float assessment) {
		this.assessment = assessment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(Integer bedrooms) {
		this.bedrooms = bedrooms;
	}

	public Integer getBeds() {
		return beds;
	}

	public void setBeds(Integer beds) {
		this.beds = beds;
	}

	public Boolean getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(Boolean airConditioner) {
		this.airConditioner = airConditioner;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Availability> getAvailability() {
		return availability;
	}

	public void setAvailability(List<Availability> availability) {
		this.availability = availability;
	}

	public List<Reserve> getReserve() {
		return reserve;
	}

	public void setReserve(List<Reserve> reserve) {
		this.reserve = reserve;
	}

	@Override
	public String toString() {
		return "Housing [id=" + id + ", user=" + user + ", name=" + name + ", assessment="
				+ assessment + ", description=" + description + ", bedrooms=" + bedrooms + ", beds=" + beds
				+ ", airConditioner=" + airConditioner + "]";
	}
}
