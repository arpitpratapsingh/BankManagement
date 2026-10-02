package com.jspiders.bankmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.bankmanagement.dto.AddressUpdateDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	AddressService addressService;

	// 1. get address by Id
	@GetMapping("/id/{addressId}")
	public ResponseEntity<ResponseStructure<Address>> getAddressById(@PathVariable int addressId) {
		return addressService.getAddressById(addressId);

	}

	// 2. update address
	@PutMapping("/{addressId}")
	public ResponseEntity<ResponseStructure<Address>> updateAddress(@RequestBody AddressUpdateDto addressDto,
			@PathVariable int addressId) {
		return addressService.updateAddress(addressDto, addressId);
	}

	// 3. get address by bank
	@GetMapping("/bankid/{bankId}")
	public ResponseEntity<ResponseStructure<Address>> getByBank(@PathVariable int bankId) {

		return addressService.getByBank(bankId);
	}

	// 4. get address by city
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Address>>> getByCity(@PathVariable String city) {
		return addressService.getByCity(city);
	}

	// 5. get address by city and street
	@GetMapping("/cityandstreet")
	public ResponseEntity<ResponseStructure<List<Address>>> getByCityAndStreet(@RequestParam String city,
			@RequestParam String street) {
		return addressService.getByCityAndStreet(city, street);
	}

	// 6. get all existing address
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Address>>> getAllAddress() {
		return addressService.getAllAddress();
	}

}
