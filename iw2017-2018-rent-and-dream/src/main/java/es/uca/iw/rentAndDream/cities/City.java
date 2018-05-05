package es.uca.iw.rentAndDream.cities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.uca.iw.rentAndDream.countries.Country;

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
	
	protected City() {
	}

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
}