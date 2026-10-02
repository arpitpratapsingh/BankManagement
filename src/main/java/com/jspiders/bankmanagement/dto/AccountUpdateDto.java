package com.jspiders.bankmanagement.dto;

import com.jspiders.bankmanagement.enums.AccountType;

public class AccountUpdateDto {
	private String accountHolderName;
	private AccountType accountType;

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

}
