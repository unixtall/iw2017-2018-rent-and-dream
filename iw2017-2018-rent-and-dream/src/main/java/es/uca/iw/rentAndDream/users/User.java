package es.uca.iw.rentAndDream.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.reserves.Reserve;

@Entity
public class User implements UserDetails{
	
	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String lastName;
	
	private String username;
	
	private String email;

	private String password;
	
	private LocalDate birthDate;
	
	private String dni;
	
	private String telephone;
	
	private LocalDate registerDate;
	
	private RoleType role;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<Housing> housing;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<Reserve> reserve;

	protected User() {
	}

	public User(String firstName, String lastName, String username, String email, LocalDate birthDate, String dni, String telephone, RoleType role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.birthDate = birthDate;
		this.dni = dni;
		this.telephone = telephone;
		this.role = role;
		this.registerDate = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(String username) {
		this.username= username;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public LocalDate getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, firstName='%s', lastName='%s', username='%s', password='%s']", id,
				firstName, lastName,username,password);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(this.role.toString()));
		return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	public List<Housing> getHousing() {
		return housing;
	}

	public void setHousing(List<Housing> housing) {
		this.housing = housing;
	}

	public List<Reserve> getReserve() {
		return reserve;
	}

	public void setReserve(List<Reserve> reserve) {
		this.reserve = reserve;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}