package com.bank.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.app.exception.InsufficientBalanceException;
import com.bank.app.exception.ValidationFailedException;
import com.bank.app.model.Account;
import com.bank.app.model.Transaction;
import com.bank.app.model.Transaction.StatusEnum;
import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.TransactionRepositary;
import com.bank.app.service.ITransactionService;

@Service
public class TransactionServiceImpl implements ITransactionService {

	@Autowired 
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepositary transactionRepositary;
	
	public List<Transaction> getAllTxtz(){
		List<Transaction> listtxtz = null;
		listtxtz = transactionRepositary.findAll();
		return listtxtz;
	}

	public Integer getNewTransactionId() {
		
		Integer firstTxtzId = 1231;
		Integer newTxtzId = 0;
		
		List<Transaction> listtxtz = getAllTxtz();
		
		if(listtxtz.isEmpty())
			newTxtzId = firstTxtzId;
		
		for(Transaction txtz : listtxtz) {
			newTxtzId = txtz.getTransactionId()+1;				
		}
		return newTxtzId;
	}
		
	public Date getCurrentDate() {    
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();  

		return date;  
	} 
	
	public Transaction createTxtz(Account accNo, Float amt, StatusEnum txtzType) {
		
		Transaction txtz = null;
		
		txtz = new Transaction();
		
		txtz.setTransactionId(getNewTransactionId());
		txtz.setTransactionType(txtzType);
		txtz.setAccountNo(accNo.getAccountNo());
		txtz.setUserName(accNo.getUserName());
		txtz.setTransactionAmount(amt);
		txtz.setTrtzDate(getCurrentDate());
		txtz.setClosingBalance(accNo.getAccBalance());
		
		return txtz;
	}
	
	@Override
	public String depositBalance(Integer accountNo, Float balance) {
		
		Account acc = null;
		Transaction txtz = null;
		
		try {
			acc = accountRepository.findById(accountNo).get();
			acc.setAccBalance(acc.getAccBalance()+balance);
			accountRepository.save(acc);
			
			txtz = createTxtz(acc, balance, StatusEnum.debit);
			
			transactionRepositary.save(txtz);
		}catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Account Number !!!");
        }

		return balance+" Rs deposited in the account no : " + acc.getAccountNo();
	}

	@Override
	public String withdrawBalance(Integer accountNo, Float balance) {
		Account acc = null;
		Transaction txtz = null;
		
		try {
			acc = accountRepository.findById(accountNo).get();
			if(acc.getAccBalance() >= balance) {
				acc.setAccBalance(acc.getAccBalance()+balance);
				accountRepository.save(acc);
				
				txtz = createTxtz(acc, balance, StatusEnum.credit);
				
				transactionRepositary.save(txtz);
			}
			else
				throw new InsufficientBalanceException("You don't have sufficient balance!!!  Current balance is " + acc.getAccBalance());
				
		}catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Account Number !!!");
        }

		return balance+" Rs withdrow from account no : " + acc.getAccountNo();
	}

	@Override
	public Transaction transferBalance(Integer accountNo, Integer beneficiaryAccNo, Float balance) {
		
		Account account = null;
        Account beneficiaryAcc = null;
        Transaction txtzSender = null;
        Transaction txtzReciver = null;

        if(accountNo != beneficiaryAccNo){

            account = accountRepository.findById(accountNo).get();
            beneficiaryAcc = accountRepository.findById(beneficiaryAccNo).get();
            if (account.getAccBalance() >= balance) {

                account.setAccBalance(account.getAccBalance() - balance);
                
                txtzSender = new Transaction();
                txtzSender.setTransactionId(getNewTransactionId());
                txtzSender.setAccountNo(accountNo);
                txtzSender.setUserName(account.getUserName());
                txtzSender.setBeneficiaryAccountNo(beneficiaryAccNo);
                txtzSender.setTransactionType(Transaction.StatusEnum.credit);
                txtzSender.setTrtzDate(getCurrentDate());
                txtzSender.setTransactionAmount(balance);
                txtzSender.setClosingBalance(account.getAccBalance());
                transactionRepositary.save(txtzSender);
                accountRepository.save(account);

                beneficiaryAcc.setAccBalance(beneficiaryAcc.getAccBalance() + balance);
                txtzReciver = new Transaction();
                txtzReciver.setTransactionId(getNewTransactionId());
                txtzReciver.setAccountNo(beneficiaryAccNo);
                txtzReciver.setUserName(beneficiaryAcc.getUserName());
                txtzReciver.setBeneficiaryAccountNo(accountNo);
                txtzReciver.setTransactionType(Transaction.StatusEnum.debit);
                txtzReciver.setTrtzDate(getCurrentDate());
                txtzReciver.setTransactionAmount(balance);
                txtzReciver.setClosingBalance(beneficiaryAcc.getAccBalance());
                transactionRepositary.save(txtzReciver);
                accountRepository.save(beneficiaryAcc);
            } else {
                throw new InsufficientBalanceException("You don't have sufficient balance!!!  Current balance is " + account.getAccBalance());
            }

        }
        else{
            throw new ValidationFailedException("Sender Account Number and Reciver Account Number is save plz enter valid Reciver Account Number");
        }
        return txtzSender;
	}
}
