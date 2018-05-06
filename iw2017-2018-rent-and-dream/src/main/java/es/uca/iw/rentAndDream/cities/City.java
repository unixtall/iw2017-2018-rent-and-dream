package es.uca.iw.rentAndDream.cities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity; 
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.uca.iw.rentAndDream.countries.Country;
import es.uca.iw.rentAndDream.housing.Housing;

@Entity
public class City {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String province;
	
	private Integer postalCode;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    public Country country;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="city")
    private List<Housing> housing;
	
	protected City() {}

	public City(String name, String province, Integer postalCode) {
		this.name = name;
		this.province = province;
		this.postalCode = postalCode;
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
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	} 
	
	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
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
}