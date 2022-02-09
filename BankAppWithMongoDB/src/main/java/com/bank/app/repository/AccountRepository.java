package com.bank.app.repository;

import com.bank.app.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, Integer>{

	Account findByAccountNo(Integer accountNo);
	Account findByUserName(String userName);
} 