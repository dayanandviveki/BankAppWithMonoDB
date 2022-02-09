package com.bank.app.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.app.exception.ValidationFailedException;
import com.bank.app.model.Account;
import com.bank.app.model.User;
import com.bank.app.repository.AccountRepository;
import com.bank.app.service.IAccountService;
import com.bank.app.service.IUserService;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private IUserService userService;
	
	public List<Account> getAllAcc(){
		List<Account> listAccounts = null;
		listAccounts = accountRepository.findAll();
		return listAccounts;
	}

	public Integer getNewAccountNo() {
		
		Integer firstAccNo = 123123123;
		Integer newAccNo = 0;
		
		List<Account> listAccounts = getAllAcc();
		
		if(listAccounts.isEmpty())
			newAccNo = firstAccNo;
		
		for(Account acc : listAccounts) {
			newAccNo = acc.getAccountNo()+1;				
		}
		return newAccNo;
	}
	
	public boolean isDublicateAccount(Account account) {
		boolean flag = false;
		List<Account> listAccounts = getAllAcc();
		for(Account acc : listAccounts) {
			if(acc.getUserName().equals(account.getUserName()) && acc.getAccountType().equals(account.getAccountType()))
				flag = true;
			
		}
		return flag;
	}
	
	@Override
	public Account createAccount(Account account) {
		
		User findUser = null;
		Account newAccount = null;
		try {
			findUser = userService.getUserByUsername(account.getUserName());
			
			if(!findUser.getIsActive())
				throw new ValidationFailedException("OTP Validation Not Done By user ...");
			else if(!isDublicateAccount(account)) {
				account.setAccountNo(getNewAccountNo());
				newAccount = accountRepository.insert(account);
			}
			else
				throw new ValidationFailedException("Already Account creater By "+account.getUserName()+" this username and "+ account.getAccountType()+ " this type plz use that Accont only");
		}catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Username !!!");
        }
		return newAccount;
	}

	@Override
	public Account getAccountByAccountNo(Integer accountNo) {

		Account account = null;
		
		try {
			account = accountRepository.findByAccountNo(accountNo);
		}catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Account No !!!");
        }
		return account;
	}

	@Override
	public Account getByUserName(String userName, String password) {
		
		User findUser = null;
		Account account = null;
		try {
			findUser = userService.getUserByUsername(userName);

			if(findUser.getPassword().equals(password)) {
				try {
					account = accountRepository.findByUserName(userName);
				}catch(NoSuchElementException nsee){
		            throw new ValidationFailedException("Account Not avaible By this Username !!!");
		        }
			}
			else
				throw new ValidationFailedException("Invalid Username or Password !!!");
		}catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Username !!!");
        }
		return account;
	}

	@Override
	public List<Account> getAllAccount() {
		return getAllAcc();
	}

	@Override
	public String getAccBalByAccNo(Integer accNo) {
		Account acc = getAccountByAccountNo(accNo);
		return "Your current Balance is : "+acc.getAccBalance()+" Rs";
	}

	
}
