package com.jspiders.bankmanagement.exception;

public class BankNotFoundException extends RuntimeException{
	public BankNotFoundException(String message){
		super(message);
	}
}
