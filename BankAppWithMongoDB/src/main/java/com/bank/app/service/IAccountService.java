package com.bank.app.service;

import java.util.List;

import com.bank.app.model.Account;

public interface IAccountService {

	Account createAccount(Account account);
	Account getAccountByAccountNo(Integer accountNo);
	Account getByUserName(String userName,String password);
	List<Account> getAllAccount();
	String getAccBalByAccNo(Integer accNo);
}
