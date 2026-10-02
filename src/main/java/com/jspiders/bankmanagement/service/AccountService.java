package com.jspiders.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.exception.InvalidRequestException;
import com.jspiders.bankmanagement.repository.AccountRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	BankRepository bankRepository;

	// 1. create account
	public ResponseEntity<ResponseStructure<Account>> createAccount(AccountCreateDto account, int bankId) {

		Optional<Account> opt = accountRepository.findByAccountNumber(account.getAccountNumber());

		if (opt.isPresent()) {

			throw new AccountAlreadyExistsException("Account already exists with given account number");

		}

		Optional<Bank> bankOpt = bankRepository.findById(bankId);

		// Check whether the bank exists
		if (bankOpt.isEmpty()) {
			throw new BankNotFoundException("Bank with this id does not exist");
		}
		// checking if user wants to create an account with negative balance
		if (account.getBalance() < 0) {
			throw new InvalidRequestException("Account balance cannot be negative");
		}

		if (account.getAccountHolderName() == null || account.getAccountHolderName().trim().isEmpty()) {
			throw new InvalidRequestException("Account holder name cannot be empty");
		}

		if (account.getAccountType() == null) {
			throw new InvalidRequestException("Account type cannot be null");
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

		return new ResponseEntity<ResponseStructure<Account>>(response, HttpStatus.CREATED);

	}

	// 2. get all accounts
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {

		List<Account> accounts = accountRepository.findAll();

		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// for checking purpose get by account number
	public ResponseEntity<ResponseStructure<Account>> getAccountByAccountNumber(long accountNumber) {

		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("account does not exixt");
		}
		ResponseStructure<Account> response = new ResponseStructure<Account>();
		response.setMessage("Account retrieved");
		response.setData(opt.get());

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 3. get account by id
	public ResponseEntity<ResponseStructure<Account>> getAccountById(int id) {

		Optional<Account> opt = accountRepository.findById(id);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("Account doesn't exist");
		}
		ResponseStructure<Account> response = new ResponseStructure<Account>();

		response.setMessage("Accounts retrieved");
		response.setData(opt.get());

		return new ResponseEntity<ResponseStructure<Account>>(response, HttpStatus.OK);
	}

	// 4. delete account by id
	public ResponseEntity<ResponseStructure<Void>> deleteAccountById(int id) {

		Optional<Account> opt = accountRepository.findById(id);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("Account doesn't exist");
		}

		accountRepository.deleteById(id);

		ResponseStructure<Void> response = new ResponseStructure<>();

		response.setMessage("Accounts deleted successfully");
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 5. update account type and holder name
	public ResponseEntity<ResponseStructure<Account>> updatePartially(int id, AccountUpdateDto updateDto) {

		Optional<Account> opt = accountRepository.findById(id);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("Account with this id does not exist");
		}

		if (updateDto.getAccountHolderName() == null && updateDto.getAccountType() == null) {
			throw new InvalidRequestException("At least one field must be provided for update");
		}

		if (updateDto.getAccountHolderName() != null && updateDto.getAccountHolderName().trim().isEmpty()) {
			throw new InvalidRequestException("Account holder name cannot be empty");
		}

		Account account = opt.get();

		// update only the fields provided
		if (updateDto.getAccountHolderName() != null) {
			account.setAccountHolderName(updateDto.getAccountHolderName().trim());
		}
		if (updateDto.getAccountType() != null) {
			account.setAccountType(updateDto.getAccountType());
		}

		accountRepository.save(account);

		ResponseStructure<Account> response = new ResponseStructure<Account>();
		response.setMessage("Account updated successfully");
		response.setData(account);

		return new ResponseEntity<ResponseStructure<Account>>(response, HttpStatus.OK);
	}

	// 6. Deposit amount
	@Transactional
	public ResponseEntity<ResponseStructure<String>> amountDeposit(AmountDto amountDto, long accountNumber) {

		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("Invalid account number");
		}

		if (amountDto == null) {
			throw new InvalidRequestException("Deposit amount is required");
		}

		if (amountDto.getAmount() <= 0) {
			throw new InvalidRequestException("Deposit amount must be positive");
		}

		Account account = opt.get();

		double currentBalance = account.getBalance();
		double deposit = amountDto.getAmount();
		double updatedBalance = currentBalance + deposit;
		account.setBalance(updatedBalance);

		accountRepository.save(account);

		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setMessage("Amount deposited successfully");
		response.setData("Balance: " + updatedBalance);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 7. Withdraw amount
	@Transactional
	public ResponseEntity<ResponseStructure<String>> amountWithdraw(AmountDto amountDto, long accountNumber) {

		Optional<Account> opt = accountRepository.findByAccountNumber(accountNumber);

		if (opt.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		if (amountDto == null) {
			throw new InvalidRequestException("Withdrawal amount is required");
		}

		Account account = opt.get();

		if (amountDto.getAmount() <= 0) {
			throw new InvalidRequestException("Withdrawal amount must be positive");
		}

		if (amountDto.getAmount() > account.getBalance()) {
			throw new InvalidRequestException("Insufficient balance");
		}
		double currentBalance = account.getBalance();
		double withdrawal = amountDto.getAmount();
		double updatedBalance = currentBalance - withdrawal;

		account.setBalance(updatedBalance);

		accountRepository.save(account);

		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Amount withdrawn successfully");
		response.setData("Balance amount is : " + updatedBalance);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 8. Transfer Amount
	@Transactional
	public ResponseEntity<ResponseStructure<String>> transferAmount(AmountTransferDto transferDto) {

		if (transferDto == null) {
			throw new InvalidRequestException("Transfer details are required");
		}
		if (transferDto.getFromAccountNumber() == transferDto.getToAccountNumber()) {
			throw new InvalidRequestException(
					"Sender and receiver accounts cannot be the same");
		}

		Optional<Account> sender = accountRepository.findByAccountNumber(transferDto.getFromAccountNumber());
		Optional<Account> reciever = accountRepository.findByAccountNumber(transferDto.getToAccountNumber());

		if (sender.isEmpty()) {
			throw new AccountNotFoundException("Sender's account does not exist");
		}
		if (reciever.isEmpty()) {
			throw new AccountNotFoundException("Beneficary's account does not exist");
		}

		Account senderAccount = sender.get();
		Account recieverAccount = reciever.get();

		if (transferDto.getAmount() <= 0) {
			throw new InvalidRequestException("Transfer amount must be positive");
		}

		if (transferDto.getAmount() > senderAccount.getBalance()) {
			throw new InvalidRequestException("Insufficient balance");
		}

		// Sender
		double updatedSenderBalance = senderAccount.getBalance() - transferDto.getAmount();
		senderAccount.setBalance(updatedSenderBalance);

		// Receiver
		double updatedReceiverBalance = recieverAccount.getBalance() + transferDto.getAmount();
		recieverAccount.setBalance(updatedReceiverBalance);

		accountRepository.save(senderAccount);
		accountRepository.save(recieverAccount);

		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setMessage("Transfer successful");
		response.setData("Sender's balance is : " + updatedSenderBalance + "\nReciever's balance is : "
				+ updatedReceiverBalance);

		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.OK);
	}

	// 9. Get account by bank
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountByBank(int bankId) {
		Optional<Bank> bankOpt = bankRepository.findById(bankId);

		if (bankOpt.isEmpty()) {
			throw new BankNotFoundException("Bank with this id does not exist");
		}

		List<Account> accounts = accountRepository.findByBankBankId(bankId);

		if (accounts.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 10. get account type
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountByAccountType(AccountType accountType) {
		if (accountType == null) {
			throw new InvalidRequestException("Account type is required");
		}

		List<Account> accounts = accountRepository.findByAccountType(accountType);

		if (accounts.isEmpty()) {
			throw new AccountNotFoundException("Account does not exist");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 11. get account with balance greater than a value
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountBalanceGreaterThan(double balance) {

		if (balance < 0) {
			throw new InvalidRequestException("Balance cannot be negative");
		}

		List<Account> accounts = accountRepository.findByBalanceGreaterThan(balance);

		if (accounts.isEmpty()) {
			throw new AccountNotFoundException("Account with balance greater than : " + balance + " does not exist");
		}

		ResponseStructure<List<Account>> response = new ResponseStructure<List<Account>>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 12. Get account by pagination and sorting
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByPagination(
			int pageNo, int pageSize, String sortBy, String sortDir) {

		if (pageNo < 0) {
			throw new InvalidRequestException(
					"Page number cannot be negative");
		}

		if (pageSize <= 0) {
			throw new InvalidRequestException(
					"Page size must be greater than 0");
		}

		if (sortBy == null || sortBy.trim().isEmpty()) {
			throw new InvalidRequestException(
					"Sort field is required");
		}

		if (!sortBy.equals("accountId") &&
				!sortBy.equals("accountNumber") &&
				!sortBy.equals("accountHolderName") &&
				!sortBy.equals("accountType") &&
				!sortBy.equals("balance")) {

			throw new InvalidRequestException(
					"Invalid sort field");
		}

		if (sortDir == null) {
			throw new InvalidRequestException(
					"Sort direction is required");
		}

		Sort sort;

		if (sortDir.equalsIgnoreCase("ASC")) {
			sort = Sort.by(sortBy).ascending();
		} else if (sortDir.equalsIgnoreCase("DESC")) {
			sort = Sort.by(sortBy).descending();
		} else {
			throw new InvalidRequestException(
					"Sort direction must be ASC or DESC");
		}

		List<Account> accounts = accountRepository
				.findAll(PageRequest.of(pageNo, pageSize, sort))
				.getContent();

		if (accounts.isEmpty()) {
			throw new AccountNotFoundException(
					"No accounts found for the given page");
		}

		ResponseStructure<List<Account>> response = new ResponseStructure<>();
		response.setMessage("Accounts retrieved");
		response.setData(accounts);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
