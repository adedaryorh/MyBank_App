package com.adedayo.AdexBank.service.impl;

import com.adedayo.AdexBank.dto.TransactionDTO;
import com.adedayo.AdexBank.model.Transaction;
import com.adedayo.AdexBank.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TransactionImpl implements TransactionService{
    @Autowired
    TransactionRespository transactionRespository;
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .status("SUCCESS")
                .build();
        transactionRespository.save(transaction);
        System.out.println("Transaction saved successfully");
    }
}
