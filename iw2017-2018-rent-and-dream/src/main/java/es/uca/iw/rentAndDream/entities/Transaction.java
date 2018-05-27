package es.uca.iw.rentAndDream.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Transaction {
	
		@Id
		@GeneratedValue
		private Long id;
		private TransactionType type;
		private LocalDateTime dateTime;
		@Column(length=500)
		private String invoice;
		private Float amount;
		
	    @ManyToOne(fetch=FetchType.LAZY)
		private User host;

	    @ManyToOne(fetch=FetchType.LAZY)
		private User guest;
	    
	    @ManyToOne(fetch=FetchType.LAZY)
	    private Reserve reserve;
	    
	    public Transaction() {}
	    
		public Transaction(User host, User guest, Reserve reserve) {
			super();
			this.host = host;
			this.guest = guest;
			this.reserve = reserve;
		}




		public Long getId() {
			return id;
		}



		public User getHost() {
			return host;
		}




		public void setHost(User host) {
			this.host = host;
		}




		public User getGuest() {
			return guest;
		}




		public void setGuest(User guest) {
			this.guest = guest;
		}




		public Reserve getReserve() {
			return reserve;
		}




		public void setReserve(Reserve reserve) {
			this.reserve = reserve;
		}




		public TransactionType getType() {
			return type;
		}




		public void setType(TransactionType type) {
			this.type = type;
		}




		public LocalDateTime getDateTime() {
			return dateTime;
		}

		public void setDateTime(LocalDateTime date) {
			this.dateTime = date;
		}

		
		public String getInvoice() {
			return invoice;
		}

		public void setInvoice(String invoice) {
			this.invoice = invoice;
		}

		public Float getAmount() {
			return amount;
		}

		public void setAmount(Float amount) {
			this.amount = amount;
		}

		@Override
		public String toString() {
			return id.toString();
		}
		
		

}
