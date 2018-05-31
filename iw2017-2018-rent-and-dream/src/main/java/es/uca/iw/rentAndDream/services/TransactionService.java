package es.uca.iw.rentAndDream.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.rentAndDream.entities.Transaction;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.repositories.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepo;
	
	public List<Transaction> findAll()
	{
		return transactionRepo.findAll();
	}
	
	public Transaction findOne(Long id)
	{
		return transactionRepo.findOne(id);
	}
	
	public void save(Transaction transaction)
	{
		transactionRepo.save(transaction);
	}
	
	public List<Transaction> findAllWithGuestAndHostAndReserve()
	{
		return transactionRepo.findAllWithGuestAndHostAndReserve();
	}
	
	public Transaction findOneWithGuestAndHostAndReserve(Long id) 
	{
		return transactionRepo.findOneWithGuestAndHostAndReserve(id);
	}

	public float addTransactionTotal() {
		return transactionRepo.addTransactionTotal();
		
	}
	
	//Del usuario
	public List<Transaction> findAllMyTransactions(User u)
	{
		return transactionRepo.findAllAllMyTransactions(u);
	}
	
	
}


