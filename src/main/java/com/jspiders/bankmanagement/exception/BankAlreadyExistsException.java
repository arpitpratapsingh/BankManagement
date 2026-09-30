package com.jspiders.bankmanagement.exception;

public class BankAlreadyExistsException extends RuntimeException{
	public BankAlreadyExistsException(String message){
		super(message);
	}
}
