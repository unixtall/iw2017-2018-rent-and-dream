package es.uca.iw.rentAndDream.countries;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Country {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private double IVA;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="country")
    public List<City> cities;

	public Country(Long id, String name, double iVA) {
		super();
		this.id = id;
		this.name = name;
		IVA = iVA;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getIVA() {
		return IVA;
	}

	public void setIVA(double iVA) {
		IVA = iVA;
	}
	
	
	
}
