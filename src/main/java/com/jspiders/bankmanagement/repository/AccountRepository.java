package com.jspiders.bankmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.bankmanagement.entity.Account;
import com.jspiders.bankmanagement.enums.AccountType;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
	Optional<Account> findByAccountNumber(long accountNumber);
	
	List<Account> findByBankBankId(int bankId);
	
	List<Account> findByAccountType(AccountType accountType);
	
	List<Account> findByBalanceGreaterThan(double balance);
}
