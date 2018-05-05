package es.uca.iw.rentAndDream.countries;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import es.uca.iw.rentAndDream.cities.City;

@Entity
public class Country {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private float IVA;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="country")
    public List<City> cities;

	public Country(String name, float iVA) {
		this.name = name;
		IVA = iVA;
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

	public float getIVA() {
		return IVA;
	}

	public void setIVA(float iVA) {
		IVA = iVA;
	}
	
	
	
}
