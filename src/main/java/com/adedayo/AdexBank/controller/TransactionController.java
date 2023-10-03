package com.adedayo.AdexBank.controller;

import com.adedayo.AdexBank.model.Transaction;
import com.adedayo.AdexBank.service.impl.BankStatement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
public class TransactionController {

    private BankStatement bankStatement;
    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate){
        return bankStatement.generateStatement(accountNumber, startDate, endDate);

    }
}
