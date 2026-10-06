package com.abw.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import com.abw.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
