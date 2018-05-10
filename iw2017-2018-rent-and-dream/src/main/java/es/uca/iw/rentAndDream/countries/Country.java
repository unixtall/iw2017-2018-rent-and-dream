package es.uca.iw.rentAndDream.countries;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import es.uca.iw.rentAndDream.cities.City;
import es.uca.iw.rentAndDream.regions.Region;

@Entity
public class Country {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	private String code;

	private Float vat;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="country")
    public List<City> cities;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="country")
    public List<Region> region;

	public Country(String name, String code, Float vat) {
		this.name = name;
		this.vat = vat;
	}

	public Country() {}
	
	public Long getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Region> getRegion() {
		return region;
	}

	public void setRegion(List<Region> region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
