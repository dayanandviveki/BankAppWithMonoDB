package com.bank.app.repository;

import com.bank.app.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepositary extends MongoRepository<Transaction, Integer>{

} 