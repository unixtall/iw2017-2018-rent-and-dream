package es.uca.iw.rentAndDream.regions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.uca.iw.rentAndDream.countries.Country;

@Entity
public class Region {

	@Id
	@GeneratedValue
	private Long id;	
	private String name;	
	private String code;	
	
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
	
	@Override
	public String toString() {
		return name;
	}
}
