package com.jspiders.bankmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.bankmanagement.dto.AccountCreateDto;
import com.jspiders.bankmanagement.dto.AccountUpdateDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Account;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.exception.AccountAlreadyExistsException;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.repository.AccountRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	@PostMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<Account>> createAccount(@RequestBody AccountCreateDto account,@PathVariable int bankId){
		
		Optional<Account> opt = accountRepository.findByAccountNumber(account.getAccountNumber());
		
		if(opt.isPresent()) {
			
			throw new AccountAlreadyExistsException("Account already exists with given account number");
			
		}
		
		Optional<Bank> bankOpt = bankRepository.findById(bankId);
		
		if(bankOpt.isEmpty()) {
			throw new BankNotFoundException("Bank with this id does not exist");
		}
		
		Account newAccount = new Account();
		
		newAccount.setAccountNumber(account.getAccountNumber());
		newAccount.setAccountHolderName(account.getAccountHolderName());
		newAccount.setAccountType(account.getAccountType());
		newAccount.setBalance(account.getBalance());
		
		newAccount.setBank(bankOpt.get());
		
		accountRepository.save(newAccount);
		
		ResponseStructure<Account> response = new ResponseStructure<>();
		response.setMessage("Account Created");
		response.setData(newAccount);
		
		return new ResponseEntity<ResponseStructure<Account>>(response,HttpStatus.CREATED);
		
	}
	
	// get all
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts(){
		
		List<Account> accounts = accountRepository.findAll();
		
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	// get by id
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Account>> getAccountById(@PathVariable int id){
		
		Optional<Account> opt = accountRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new AddressNotFoundException("Account doesn't exist");
		}
		ResponseStructure<Account> response = new ResponseStructure<Account>();
		
		response.setMessage("Accounts retrieved");
		response.setData(opt.get());
		
		return new ResponseEntity<ResponseStructure<Account>>(response,HttpStatus.OK);
	}
	
	// delete
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteAccountById(@PathVariable int id){
		
		Optional<Account> opt = accountRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new AddressNotFoundException("Account doesn't exist");
		}
		
		accountRepository.deleteById(id);
		
		ResponseStructure<Void> response = new ResponseStructure<>();
		
		response.setMessage("Accounts retrieved");
		response.setData(null);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PatchMapping("/update/partialupdate")
	public ResponseEntity<ResponseStructure<Account>> updatePartially(@PathVariable int id, @RequestBody AccountUpdateDto updateDto){
		
		Optional<Account> opt = accountRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new AddressNotFoundException("Account with this id does not exist");
		}
		
		Account account = opt.get();
		
		// update only the fields provided
		if(updateDto.getAccountHolderName() != null) {
			account.setAccountHolderName(updateDto.getAccountHolderName());
		}
		if(updateDto.getAccountType() != null) {
			account.setAccountType(updateDto.getAccountType());
		}
		
		accountRepository.save(account);
		
		ResponseStructure<Account> response = new ResponseStructure<Account>();
		response.setMessage("Account updated successfully");
		response.setData(account);
		
		return new ResponseEntity<ResponseStructure<Account>>(response,HttpStatus.OK);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
