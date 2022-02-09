package com.bank.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.app.model.Transaction;
import com.bank.app.service.ITransactionService;

@RestController
@RequestMapping("/txtz")
public class TransactionController {

	@Autowired
	private ITransactionService txtzService;
	
	@PostMapping("/debit/{accno}/{amt}")
	public ResponseEntity<String> debitAmount(@PathVariable("accno") Integer accNo,@PathVariable("amt") Float amt) {
		return new ResponseEntity<String>(txtzService.depositBalance(accNo, amt),HttpStatus.CREATED);
	}
	
	@PostMapping("/credit/{accno}/{amt}")
	public ResponseEntity<String> creditAmount(@PathVariable("accno") Integer accNo,@PathVariable("amt") Float amt) {
		return new ResponseEntity<String>(txtzService.withdrawBalance(accNo, amt),HttpStatus.CREATED);
	}
	
	@PatchMapping("/update/transfer/{accountNo}/{beneficiaryAccNo}/{balance}")
    public ResponseEntity<Transaction> credit(@Valid @PathVariable Integer accountNo,@PathVariable Integer beneficiaryAccNo,@PathVariable Float balance) {
        return new ResponseEntity<Transaction>(txtzService.transferBalance(accountNo ,beneficiaryAccNo, balance),HttpStatus.OK);
    }

	
}
