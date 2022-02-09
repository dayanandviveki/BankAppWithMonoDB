package com.bank.app.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.app.model.Account;
import com.bank.app.model.User;
import com.bank.app.service.IAccountService;

@RestController
@RequestMapping("/account")
public class AccountControlller {

    @Autowired
    private IAccountService accountService;

    @PostMapping("/saveaccount")
    public ResponseEntity<Account> createUser(@Valid @RequestBody Account account) {
        return new ResponseEntity<Account>(accountService.createAccount(account),HttpStatus.CREATED);
    }
    
    @GetMapping("/getaccbyaccno/{accno}")
    public ResponseEntity<Account> getAccByAccNo(@PathVariable("accno") Integer accno){
    	return new ResponseEntity<Account>(accountService.getAccountByAccountNo(accno),HttpStatus.OK);
    }

    @GetMapping("/getaccbyusername")
    public ResponseEntity<Account> getAccByUsernameAndPassword(@RequestBody User user){
    	return new ResponseEntity<Account>(accountService.getByUserName(user.getUserName(),user.getPassword()),HttpStatus.OK);
    }
    
    @GetMapping("/getaccbalbyaccno/{accno}")
    public ResponseEntity<String> getAccBalByAccNo(@PathVariable("accno") Integer accNo){
    	return new ResponseEntity<String>(accountService.getAccBalByAccNo(accNo),HttpStatus.OK);
    }

    @GetMapping("/getAllAccount")
    public ResponseEntity<List<Account>> getAllAccDetails(){
    	return new ResponseEntity<List<Account>>(accountService.getAllAccount(),HttpStatus.OK);
    }
    
    
}