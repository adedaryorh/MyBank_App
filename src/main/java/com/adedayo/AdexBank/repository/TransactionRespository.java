package com.adedayo.AdexBank.repository;

import com.adedayo.AdexBank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRespository extends JpaRepository<Transaction, String> {
}
