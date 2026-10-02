package com.jspiders.bankmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jspiders.bankmanagement.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BankNotFoundException.class)
	public ResponseEntity<ResponseStructure<Void>> bankNotFoundException(BankNotFoundException ex) {

		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BankAlreadyExistsException.class)
	public ResponseEntity<ResponseStructure<Void>> bankAlreadyExistsException(BankAlreadyExistsException ex) {
		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<ResponseStructure<Void>>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ResponseStructure<Void>> accountNotFoundException(AccountNotFoundException ex) {

		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// account already exists exception
	@ExceptionHandler(AccountAlreadyExistsException.class)
	public ResponseEntity<ResponseStructure<Void>> accountAlreadyExistsException(AccountAlreadyExistsException ex) {

		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseEntity<ResponseStructure<Void>> addressNotFoundException(AddressNotFoundException ex) {

		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AddressAlreadyExistsException.class)
	public ResponseEntity<ResponseStructure<Void>> addressAlreadyExistsException(AddressAlreadyExistsException ex) {
		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	// Invalid Request Exception
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ResponseStructure<Void>> addressNotFoundException(InvalidRequestException ex) {

		ResponseStructure<Void> response = new ResponseStructure<Void>();

		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
