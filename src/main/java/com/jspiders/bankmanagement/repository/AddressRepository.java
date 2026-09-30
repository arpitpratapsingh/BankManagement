package com.jspiders.bankmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	List<Address> findByCity(String city);
	List<Address> findByCityAndStreet(String city, String street);
}
