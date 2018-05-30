package es.uca.iw.rentAndDream.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Housing {

	@Id
	@GeneratedValue
	private Long id;	
	private String name;
	private String address;
	private Double assessment;	
	private Integer assessmentNumber;
	@Column(length=500)
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
	
	public Housing(){}
	
	public Housing(String name, String address, Double assessment, String description,
			Integer bedrooms, Integer beds, Boolean airConditioner) {
		this.name = name;
		this.address = address;
		this.assessment = assessment;
		this.description = description;
		this.bedrooms = bedrooms;
		this.beds = beds;
		this.airConditioner = airConditioner;
	}

	public Housing(String name, String address, Double assessment, String description, Integer bedrooms,
			Integer beds, Boolean airConditioner, User user, City city) {
		super();

		this.name = name;
		this.address = address;
		this.assessment = assessment;
		this.description = description;
		this.bedrooms = bedrooms;
		this.beds = beds;
		this.airConditioner = airConditioner;
		this.user = user;
		this.city = city;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public Double getAssessment() {
		return assessment;
	}

	public void setAssessment(Double assessment) {
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
	

	public Integer getAssessmentNumber() {
		return assessmentNumber;
	}

	public void setAssessmentNumber(Integer assessmentNumber) {
		this.assessmentNumber = assessmentNumber;
	}

	@Override
	public String toString() {
		return name;
	}
}
