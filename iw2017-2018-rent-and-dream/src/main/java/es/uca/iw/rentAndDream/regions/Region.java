package es.uca.iw.rentAndDream.regions;

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
public class Region {

	@Id
	@GeneratedValue
	private Long id;	
	private String name;	
	private String code;	
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="region")
    public List<City> cities;
	
    @ManyToOne(fetch=FetchType.LAZY)
    private Country country;

	protected Region(){}
	
	public Region(String name, String code) {
		this.name = name;
		this.code = code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public List<City> getCities() {
		return cities;
	}
	
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
