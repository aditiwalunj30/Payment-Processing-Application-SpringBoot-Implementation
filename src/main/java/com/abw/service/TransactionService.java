
package com.abw.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abw.dto.TransferRequestDto;
import com.abw.dto.TransferResponseDto;
import com.abw.entity.Account;
import com.abw.entity.Transaction;
import com.abw.entity.TransactionStatus;
import com.abw.exception.InsufficientFundException;
import com.abw.repo.AccountRepository;
import com.abw.repo.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public TransferResponseDto transferMoney(TransferRequestDto requestDto) {

        Account fromAccount = accountRepository.findByAccountNumber(requestDto.getFromAccountNumber())
                .orElseThrow(() -> new NoSuchElementException("Sender account not found: " + requestDto.getFromAccountNumber()));

        Account toAccount = accountRepository.findByAccountNumber(requestDto.getToAccountNumber())
                .orElseThrow(() -> new NoSuchElementException("Receiver account not found: " + requestDto.getToAccountNumber()));

        Double amount = requestDto.getAmount();

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        if (fromAccount.getBalance() < amount) {
            Transaction failedTxn = new Transaction();
            failedTxn.setFromAccountNumber(fromAccount.getAccountNumber());
            failedTxn.setToAccountNumber(toAccount.getAccountNumber());
            failedTxn.setAmount(amount);
            failedTxn.setStatus(TransactionStatus.FAILED);
            failedTxn.setRemarks("Insufficient balance");
            transactionRepository.save(failedTxn);

            throw new InsufficientFundException("Insufficient balance in account: " + fromAccount.getAccountNumber());
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        try {
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Transfer failed due to concurrent update. Please try again.");
        }

        Transaction successTxn = new Transaction();
        successTxn.setFromAccountNumber(fromAccount.getAccountNumber());
        successTxn.setToAccountNumber(toAccount.getAccountNumber());
        successTxn.setAmount(amount);
        successTxn.setStatus(TransactionStatus.SUCCESS);
        successTxn.setRemarks("Transfer successful");

        Transaction savedTxn = transactionRepository.save(successTxn);

        return convertToDto(savedTxn);
    }

    private TransferResponseDto convertToDto(Transaction txn) {
        TransferResponseDto dto = new TransferResponseDto();
        dto.setTransactionId(txn.getId());
        dto.setFromAccountNumber(txn.getFromAccountNumber());
        dto.setToAccountNumber(txn.getToAccountNumber());
        dto.setAmount(txn.getAmount());
        dto.setStatus(txn.getStatus());
        dto.setRemarks(txn.getRemarks());
        dto.setTimestamp(txn.getTimestamp());
        return dto;
    }
    
   
}