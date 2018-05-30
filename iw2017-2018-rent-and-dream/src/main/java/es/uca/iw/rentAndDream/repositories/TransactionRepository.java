package es.uca.iw.rentAndDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uca.iw.rentAndDream.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	public Transaction findOne(Long id);
	
	@Query("Select t from Transaction t "
			+ "join fetch t.guest "
			+ "join fetch t.host "
			+ "join fetch t.reserve "
			+ "where t.id = ?1")
	public Transaction findOneWithGuestAndHostAndReserve(Long id);
	
	@Query("Select t from Transaction t "
			+ "join fetch t.guest "
			+ "join fetch t.host "
			+ "join fetch t.reserve ")
	public List<Transaction> findAllWithGuestAndHostAndReserve();
	
	@Query("Select sum(t.transactionProfit) from Transaction t")
	public float addTransactionTotal();
	

}
