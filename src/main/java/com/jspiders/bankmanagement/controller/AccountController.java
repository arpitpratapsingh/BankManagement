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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.bankmanagement.dto.AccountCreateDto;
import com.jspiders.bankmanagement.dto.AccountUpdateDto;
import com.jspiders.bankmanagement.dto.AmountDto;
import com.jspiders.bankmanagement.dto.AmountTransferDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Account;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.enums.AccountType;
import com.jspiders.bankmanagement.exception.AccountAlreadyExistsException;
import com.jspiders.bankmanagement.exception.AccountNotFoundException;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.exception.InvalidRequestException;
import com.jspiders.bankmanagement.repository.AccountRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	// 1. create account
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
	
	// 2. get all accounts
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts(){
		
		List<Account> accounts = accountRepository.findAll();
		
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	// for checking purpose get by account number
	@GetMapping("/accountnumber/{accountNumber}")
	public ResponseEntity<ResponseStructure<Account>> getAccountByAccountNumber(@PathVariable long accountNumber){
		
		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);
		
		if(opt.isEmpty()) {
			throw new AccountNotFoundException("account does not exixt");
		}
		ResponseStructure<Account> response = new ResponseStructure<Account>();
		response.setMessage("Account retrieved");
		response.setData(opt.get());
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	// 3. get account by id
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Account>> getAccountById(@PathVariable int id){
		
		Optional<Account> opt = accountRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new AccountNotFoundException("Account doesn't exist");
		}
		ResponseStructure<Account> response = new ResponseStructure<Account>();
		
		response.setMessage("Accounts retrieved");
		response.setData(opt.get());
		
		return new ResponseEntity<ResponseStructure<Account>>(response,HttpStatus.OK);
	}
	
	// 4. delete account by id
	
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
	
	// 5. update account type and holder name
	
	@PatchMapping("/update/partialupdate/{id}")
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
	
	//6. Deposit amount
	@Transactional
	@PatchMapping("deposit/{accountNumber}")
	public ResponseEntity<ResponseStructure<String>> amountDeposit(@RequestBody AmountDto amountDto, @PathVariable long accountNumber){
		
		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);
		
		if(opt.isEmpty()) {
			throw new AccountNotFoundException("Invalid account number");
		}
		
		Account account = opt.get();
		
		if(amountDto.getAmount() <= 0) {
			throw new InvalidRequestException("Deposit amount must be positive");
		}
		double currentBalance = account.getBalance();
		double deposit = amountDto.getAmount();
		double updatedBalance = currentBalance+deposit;
		account.setBalance(updatedBalance);
		
		accountRepository.save(account);
		
		ResponseStructure<String> response = new ResponseStructure<String>() ;
		response.setMessage("Amount deposited");
		response.setData("Balance: "+ updatedBalance);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	
	//7. Withdraw amount
	@Transactional
	@PatchMapping("/withdraw/{accountNumber}")
	public ResponseEntity<ResponseStructure<String>> amountWithdraw(@RequestBody AmountDto amountDto, @PathVariable long accountNumber){
		
		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);
		
		if(opt.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		
		Account account = opt.get();
		
		if(amountDto.getAmount()<=0 || amountDto.getAmount()>account.getBalance()) {
			throw new InvalidRequestException("Invalid amount");
		}
		
		double currentBalance = account.getBalance();
		double withdrawal = amountDto.getAmount();
		double updatedBalance = currentBalance - withdrawal;
		
		account.setBalance(updatedBalance);
		
		accountRepository.save(account);
		
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Amount withdrew successfully");
		response.setData("Balance amount is : "+ updatedBalance);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	//8. Transfer Amount
	@Transactional
	@PatchMapping("/transfer")
	public ResponseEntity<ResponseStructure<String>> transferAmount(@RequestBody AmountTransferDto transferDto){
		
		Optional<Account> sender = accountRepository.findByAccountNumber(transferDto.getFromAccountNumber());
		Optional<Account> reciever = accountRepository.findByAccountNumber(transferDto.getToAccountNumber());
		
		if(sender.isEmpty()) {
			throw new AccountNotFoundException("Sender's account does not exist");
		}
		if(reciever.isEmpty()) {
			throw new AccountNotFoundException("Beneficary's account does not exist");
		}
		Account senderAccount = sender.get();
		Account recieverAccount = reciever.get();
		
		if(transferDto.getAmount()<=0 || transferDto.getAmount()>senderAccount.getBalance()) {
			throw new InvalidRequestException("Invalid amount");
		}
		if(transferDto.getFromAccountNumber() == transferDto.getToAccountNumber()) {
			throw new InvalidRequestException("Sender and reciever can not be same");
		}
		
		// sender logic
		double senderBalance = senderAccount.getBalance();
		double sentAmount = transferDto.getAmount();
		double updatedSenderBalance = senderBalance-sentAmount;
		
		senderAccount.setBalance(updatedSenderBalance);
		
		accountRepository.save(senderAccount);
		
		// benefactor logic
		double recieverBalance = recieverAccount.getBalance();
		double recievedAmount = transferDto.getAmount();
		double updatedRecieverBalance = recieverBalance+recievedAmount;
		
		recieverAccount.setBalance(updatedRecieverBalance);
		
		accountRepository.save(recieverAccount);
		
		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setMessage("Transfer successful");
		response.setData("Sedner's balance is : "+senderAccount.getBalance()+"\nReciever's balance is : "+recieverAccount.getBalance());
		
		return new ResponseEntity<ResponseStructure<String>>(response,HttpStatus.OK);
	}
	
	//9. Get account by bank
	@GetMapping("/all/bankid/{bankId}")
	ResponseEntity<ResponseStructure<List<Account>>> getAccountByBank(@PathVariable int bankId){
		List<Account> accounts = accountRepository.findByBankBankId(bankId);
		
		if(accounts.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//10. get account type
	@GetMapping("/all/accounttype/{accountType}")
	ResponseEntity<ResponseStructure<List<Account>>> getAccountByBank(@PathVariable AccountType accountType){
		
		
		List<Account> accounts = accountRepository.findByAccountType(accountType);
		
		if(accounts.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//11. get account with balance greater than a value
	@GetMapping("/greater/{balance}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountBalanceGreaterThan(@PathVariable double balance){
		
		List<Account> accounts = accountRepository.findByBalanceGreaterThan(balance);
		
		if(accounts.isEmpty()) {
			throw new AccountNotFoundException("Account with balance greater than : "+balance+" does not exist");
		}
		
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	
	//12. get account by pagination and sorting

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
