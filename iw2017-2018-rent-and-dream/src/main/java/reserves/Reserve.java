package reserves;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Reserve {

	@Id
	@GeneratedValue
	private Long id;
	
	private Integer number_guests;
	
	private LocalDate entry_date;
	
	private LocalDate departure_date;
	
	private float price;
	
	private boolean confirmed;
}
