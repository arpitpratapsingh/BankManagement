package com.jspiders.bankmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.jspiders.bankmanagement.enums.AccountType;
import com.jspiders.bankmanagement.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	// 1. create account
	@PostMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<Account>> createAccount(@RequestBody AccountCreateDto account,
			@PathVariable int bankId) {

		return accountService.createAccount(account, bankId);

	}

	// 2. get all accounts

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {

		return accountService.getAllAccounts();
	}

	// for checking purpose get by account number
	@GetMapping("/accountnumber/{accountNumber}")
	public ResponseEntity<ResponseStructure<Account>> getAccountByAccountNumber(@PathVariable long accountNumber) {
		return accountService.getAccountByAccountNumber(accountNumber);

	}

	// 3. get account by id
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Account>> getAccountById(@PathVariable int id) {
		return accountService.getAccountById(id);
	}

	// 4. delete account by id

	@DeleteMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteAccountById(@PathVariable int id) {
		return accountService.deleteAccountById(id);
	}

	// 5. update account type and holder name

	@PatchMapping("/update/partialupdate/{id}")
	public ResponseEntity<ResponseStructure<Account>> updatePartially(@PathVariable int id,
			@RequestBody AccountUpdateDto updateDto) {
		return accountService.updatePartially(id, updateDto);
	}

	// 6. Deposit amount
	@PatchMapping("/deposit/{accountNumber}")
	public ResponseEntity<ResponseStructure<String>> amountDeposit(@RequestBody AmountDto amountDto,
			@PathVariable long accountNumber) {
		return accountService.amountDeposit(amountDto, accountNumber);

	}

	// 7. Withdraw amount
	@PatchMapping("/withdraw/{accountNumber}")
	public ResponseEntity<ResponseStructure<String>> amountWithdraw(@RequestBody AmountDto amountDto,
			@PathVariable long accountNumber) {
		return accountService.amountWithdraw(amountDto, accountNumber);

	}

	// 8. Transfer Amount
	@PatchMapping("/transfer")
	public ResponseEntity<ResponseStructure<String>> transferAmount(@RequestBody AmountTransferDto transferDto) {
		return accountService.transferAmount(transferDto);
	}

	// 9. Get account by bank
	@GetMapping("/all/bankid/{bankId}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountByBank(@PathVariable int bankId) {
		return accountService.getAccountByBank(bankId);
	}

	// 10. get account type
	@GetMapping("/all/accounttype/{accountType}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountByAccountType(
			@PathVariable AccountType accountType) {

		return accountService.getAccountByAccountType(accountType);
	}

	// 11. get account with balance greater than a value
	@GetMapping("/greater/{balance}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountBalanceGreaterThan(@PathVariable double balance) {
		return accountService.getAccountBalanceGreaterThan(balance);
	}

	// 12. get account by pagination and sorting

	@GetMapping("/pagination")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByPagination(@RequestParam int pageNo,
			@RequestParam int pageSize,
			@RequestParam String sortBy, @RequestParam String sortDir) {
		return accountService.getAccountsByPagination(pageNo, pageSize, sortBy, sortDir);
	}

}
