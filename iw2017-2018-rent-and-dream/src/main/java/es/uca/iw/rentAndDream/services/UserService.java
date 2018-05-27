/**
 * 
 */
package es.uca.iw.rentAndDream.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.UserRepository;

/**
 * @author ruizrube
 *
 */
@Service

public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
	
	public User findByUserId(Long userId)
	{
		return repo.findByUserIdWithJoinFetch(userId);
	}
	
	public User save(User user) {

		if(user.getId() == null)
		{

			if(user.getPassword() == null)
				user.setPassword(passwordEncoder.encode("default"));
			else
				user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		else
		{
			String pass = repo.findOne(user.getId()).getPassword();
			if(!pass.equals(user.getPassword()))
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			else
				user.setPassword(pass);
		}
		return repo.save(user);
	}

	public List<User> findByLastNameStartsWithIgnoreCase(String lastName) {
		return repo.findByLastNameStartsWithIgnoreCase(lastName);
	}

	public User findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(User arg0) {
		repo.delete(arg0);
	}

	public List<User> findAll() {
		return repo.findAll();
	}
	
	public User findByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
	
	public User findByHousing(Housing h)
	{
		return repo.findByHousing(h);
	}


	
}
