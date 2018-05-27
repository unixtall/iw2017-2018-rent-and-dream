package es.uca.iw.rentAndDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.rentAndDream.entities.Country;
import es.uca.iw.rentAndDream.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	public Transaction findOne(Long id);

}
