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
	public CommandLineRunner loadData(UserService service) {
		return (args) -> {

			if (service.findAll().size() == 0) {
				// save a couple of users with default password: default
				service.save(new User("Jack", "Bauer", "Jack", "Jack@example.com", LocalDate.of(1992, 1, 19), "12345678J", "956403954", RoleType.USER));

				User root = new User("root", "root", "root", "root@example.com", LocalDate.of(1992, 1, 19), "87654321J", "678228328", RoleType.ADMIN);
				root.setPassword("root");
				service.save(root);

				// fetch all users
				log.info("Users found with findAll():");
				log.info("-------------------------------");
				for (User user : service.findAll()) {
					log.info(user.toString());
				}
				log.info("");

				// fetch an individual user by ID
				User user = service.findOne(1L);
				log.info("User found with findOne(1L):");
				log.info("--------------------------------");
				log.info(user.toString());
				log.info("");

				// fetch users by last name
				log.info("User found with findByLastNameStartsWithIgnoreCase('Bauer'):");
				log.info("--------------------------------------------");
				for (User bauer : service.findByLastNameStartsWithIgnoreCase("Bauer")) {
					log.info(bauer.toString());
				}
				log.info("");
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

			 /*auth
			 .inMemoryAuthentication()
			 .withUser("admin").password("p").roles("ADMIN", "MANAGER", "USER")
			 .and()
			 .withUser("manager").password("p").roles("MANAGER", "USER")
			 .and()
			 .withUser("user").password("p").roles("USER");*/
			
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
