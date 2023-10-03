package com.adedayo.AdexBank.service.impl;

import com.adedayo.AdexBank.dto.TransactionDTO;
import com.adedayo.AdexBank.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}
