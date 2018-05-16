package es.uca.iw.rentAndDream;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.uca.iw.rentAndDream.availabilities.Availability;
import es.uca.iw.rentAndDream.availabilities.AvailabilityService;
import es.uca.iw.rentAndDream.cities.CityService;
import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.housing.HousingService;
import es.uca.iw.rentAndDream.reserves.ReserveService;
import es.uca.iw.rentAndDream.security.VaadinSessionSecurityContextHolderStrategy;
import es.uca.iw.rentAndDream.users.RoleType;
import es.uca.iw.rentAndDream.users.User;
import es.uca.iw.rentAndDream.users.UserService;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MainApplication {

	private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserService userService, HousingService housingService, CityService cityService, AvailabilityService availabilityService,
			ReserveService reserveService) {
		return (args) -> {
			
			if (userService.findAll().size() == 0) {
				
				// save users with default password: default
				User user1 = new User("user1", "user1", "user1", "user1@example.com", LocalDate.of(1992, 1, 19), "12345678J", "956403954", RoleType.USER);
				User user2 = new User("user2", "user2", "user2", "user2@example.com", LocalDate.of(1992, 1, 19), "87654321J", "956493387", RoleType.USER);
				User manager = new User("manager", "manager", "manager", "manager@example.com", LocalDate.of(1992, 1, 19), "12345678J", "956403954", RoleType.MANAGER);
				User root = new User("root", "root", "root", "root@example.com", LocalDate.of(1992, 1, 19), "87654321J", "678228328", RoleType.ADMIN);
				root.setPassword("root");
				userService.save(user1);
				userService.save(user2);
				userService.save(manager);
				userService.save(root);	
			}
			
			if (housingService.findAll().size() == 0) {
				
				Housing housing1 = new Housing("House 1", "address", 0f, "description", 2, 2, false);
				Housing housing2 = new Housing("House 2", "address", 0f, "description", 4, 4, false);
				Housing housing3 = new Housing("House 3", "address", 0f, "description", 4, 8, false);
				Housing housing4 = new Housing("House 4", "address", 0f, "description", 1, 1, false);
				housing1.setUser(userService.loadUserByUsername("user1"));
				housing2.setUser(userService.loadUserByUsername("user2"));
				housing3.setUser(userService.loadUserByUsername("user1"));
				housing4.setUser(userService.loadUserByUsername("user2"));
				//Asignadas a la ciudad de campano
				housing1.setCity(cityService.findOne(700044L));
				housing2.setCity(cityService.findOne(700044L));
				housing3.setCity(cityService.findOne(700044L));
				housing4.setCity(cityService.findOne(700044L));
				
				Availability availability1 = new Availability(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 31), 30f);
				Availability availability2 = new Availability(LocalDate.of(2018, 6, 1), LocalDate.of(2018, 12, 31), 60f);
				Availability availability3 = new Availability(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 31), 30f);
				Availability availability4 = new Availability(LocalDate.of(2018, 6, 1), LocalDate.of(2018, 12, 31), 60f);
				Availability availability5 = new Availability(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 31), 30f);
				Availability availability6 = new Availability(LocalDate.of(2018, 6, 1), LocalDate.of(2018, 12, 31), 60f);
				Availability availability7 = new Availability(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 31), 30f);
				
				availability1.setHousing(housing1);
				availability2.setHousing(housing1);
				availability3.setHousing(housing2);
				availability4.setHousing(housing2);
				availability5.setHousing(housing3);
				availability6.setHousing(housing3);
				availability7.setHousing(housing4);
				
				housingService.save(housing1);
				housingService.save(housing2);
				housingService.save(housing3);
				housingService.save(housing4);
				availabilityService.save(availability1);
				availabilityService.save(availability2);
				availabilityService.save(availability3);
				availabilityService.save(availability4);
				availabilityService.save(availability5);
				availabilityService.save(availability6);
				availabilityService.save(availability7);
			}
			
			if (reserveService.findAll().size() == 0) {

			}
		};
	}

	@Configuration
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

		@Autowired
		private UserDetailsService userDetailsService;

		@Bean
		public PasswordEncoder encoder() {
			return new BCryptPasswordEncoder(11);
		}

		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			authProvider.setPasswordEncoder(encoder());
			return authProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.authenticationProvider(authenticationProvider());
			
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}

		static {
			// Use a custom SecurityContextHolderStrategy
			SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
		}
	}
}
