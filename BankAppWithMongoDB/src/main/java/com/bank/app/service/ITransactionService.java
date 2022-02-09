package com.bank.app.service;

import com.bank.app.model.Transaction;

public interface ITransactionService {

	 String depositBalance(Integer accountNo, Float balance);
	 String withdrawBalance(Integer accountNo,Float balance);
	 Transaction transferBalance(Integer accountNo, Integer beneficiaryAccNo, Float balance);
}
