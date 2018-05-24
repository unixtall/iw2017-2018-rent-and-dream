package es.uca.iw.rentAndDream.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( indexes = {
	    @Index(columnList = "name", unique = false)
	})
public class City {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private Float latitude;
	
	private Float longitude;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public Country country;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public Region region;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="city")
    private List<Housing> housing;
	
	protected City() {}

	public City(String name, Float latitude, Float longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Housing> getHousing() {
		return housing;
	}

	public void setHousing(List<Housing> housing) {
		this.housing = housing;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return name;
	}
}