package com.jspiders.bankmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.bankmanagement.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
	Optional<Account> findByAccountNumber(long accountNumber);
}
