package com.codercampus.Assignment11.controller;

import com.codercampus.Assignment11.domain.Transaction;
import com.codercampus.Assignment11.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @GetMapping("/transactions")
    public String display(ModelMap map) {
        List<Transaction> transactions = transactionService.findAll();
        for (Transaction trans : transactions) {
            System.out.println("type="+trans.getType());
        }
        Object transactions1 = map.put("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/transactions/{transactionId}")
    public String displayByid(@PathVariable Long transactionId, ModelMap map) {
       Transaction transaction = transactionService.findById(transactionId);
        Object transactions1 = map.put("transactions", transaction);
        return "transactionId";

    }
}