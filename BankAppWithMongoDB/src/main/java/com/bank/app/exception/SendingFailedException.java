package com.bank.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SendingFailedException extends RuntimeException{
   
	private static final long serialVersionUID = 4418620448092256134L;

	public SendingFailedException(String message) {
        super(message);
    }
}