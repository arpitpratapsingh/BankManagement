package com.jspiders.bankmanagement.exception;

public class AccountAlreadyExistsException extends RuntimeException {
	public AccountAlreadyExistsException(String message) {
		super(message);
	}

}
