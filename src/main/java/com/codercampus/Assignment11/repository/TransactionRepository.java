package com.codercampus.Assignment11.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.codercampus.Assignment11.domain.Transaction;

@Repository
public class TransactionRepository {
	private List<Transaction> transactions = new ArrayList<>(100);
	Transaction transaction = new Transaction();

	public TransactionRepository () {
		super();
		populateData();
	}

	public List<Transaction> findAll () {
		List<Transaction> sortedTransactions = transactions.stream()
				.sorted(Comparator.comparing(Transaction::getDate))
				.collect(Collectors.toList());
		calculateFundsIn(sortedTransactions);
		calculateFundsOut(sortedTransactions);
		return sortedTransactions;
	}

	@SuppressWarnings("unchecked")
	public void populateData() {
		try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/doNotTouch/transactions.doNotTouch");
			 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
			this.transactions = (List<Transaction>) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
	}

	public Transaction findById(Long transactionId) {
		return transactions.stream()
				.filter(transaction -> transaction.getId().equals(transactionId))
				.findFirst()
				.orElse(null);
	}

	public static List<Transaction> calculateFundsIn(List<Transaction> transactions) {
		// Filter transactions by type "D" to get funds in transactions
		List<Transaction> fundsInTransactions = transactions.stream()
				.filter(transaction -> "D".equals(transaction.getType()))
				.collect(Collectors.toList());

		// Group funds in transactions by retailer and calculate the sum of amounts for each retailer
		Map<String, BigDecimal> fundsInByRetailer = fundsInTransactions.stream()
				.collect(Collectors.groupingBy(Transaction::getRetailer,
						Collectors.mapping(Transaction::getAmount,
								Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

		// Set calculated values in Transaction objects
		fundsInTransactions.forEach(transaction -> {
			BigDecimal fundsIn = fundsInByRetailer.getOrDefault(transaction.getRetailer(), BigDecimal.ZERO);
			transaction.setFundsIn(fundsIn);
		});

		return fundsInTransactions;
	}
	public static List<Transaction> calculateFundsOut(List<Transaction> transactions) {
		// Filter transactions by type "D" to get funds in transactions
		List<Transaction> fundsOutTransactions = transactions.stream()
				.filter(transaction -> "C".equals(transaction.getType()))
				.collect(Collectors.toList());

		// Group funds in transactions by retailer and calculate the sum of amounts for each retailer
		Map<String, BigDecimal> fundsOutByRetailer = fundsOutTransactions.stream()
				.collect(Collectors.groupingBy(Transaction::getRetailer,
						Collectors.mapping(Transaction::getAmount,
								Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

		// Set calculated values in Transaction objects
		fundsOutTransactions.forEach(transaction -> {
			BigDecimal fundsOut = fundsOutByRetailer.getOrDefault(transaction.getRetailer(), BigDecimal.ZERO);
			transaction.setFundsOut(fundsOut);
		});

		return fundsOutTransactions;
	}



}

