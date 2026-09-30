package com.jspiders.bankmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;

import jakarta.transaction.Transactional;

public interface BankRepository extends JpaRepository<Bank, Integer> {

	//get
	Optional<Bank> findByContactNumber(long contactNumber);
	Optional<Bank> findByIfsc(String ifsc);
	Optional<Bank> findByAddress_AddressId(int addressId);
	Optional<Bank> findByAddress(Address address);
	List<Bank> findByAddress_City(String city);
	
	//exists
	boolean existsByContactNumber(long contactNumber);
	boolean existsByIfsc(String ifsc);
	
	@Modifying
	@Transactional
	void deleteByContactNumber(long contactNumber);
}
