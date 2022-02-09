package com.bank.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ValidationFailedException extends RuntimeException{

	private static final long serialVersionUID = -7425212264190292672L;

	public ValidationFailedException(String message) {
        super(message);
    }
}