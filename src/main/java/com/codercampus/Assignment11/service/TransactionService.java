package com.codercampus.Assignment11.service;

import com.codercampus.Assignment11.domain.Transaction;
import com.codercampus.Assignment11.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    public List<Transaction> findAll() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionList;
    }

    public Transaction findById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId);
        return transaction;
    }

}
