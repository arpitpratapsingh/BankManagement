package com.jspiders.bankmanagement.exception;

public class AccountNotFoundException extends RuntimeException{
	AccountNotFoundException(String message){
		super(message);
	}
}
