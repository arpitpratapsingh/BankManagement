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

import com.jspiders.bankmanagement.dto.BankUpdateDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.service.BankService;

@RequestMapping("/bank")
@RestController
public class BankController {

	@Autowired
	BankService bankService;

	// 1. create bank
	@PostMapping
	public ResponseEntity<ResponseStructure<Bank>> createBank(@RequestBody Bank bank) {
		return bankService.createBank(bank);
	}

	// 2. get all banks
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {

		return bankService.getAllBank();

	}

	// 3. get bank by id
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable int id) {
		return bankService.getBankById(id);
	}

	// 4. delete bank
	@DeleteMapping("/{contactNumber}")
	public ResponseEntity<ResponseStructure<Void>> deleteByContactNumber(@PathVariable long contactNumber) {
		return bankService.deleteByContactNumber(contactNumber);
	}

	// 5. update bank
	@PatchMapping("/update/{bankId}")
	public ResponseEntity<ResponseStructure<Bank>> updateBank(@RequestBody BankUpdateDto bankUpdateDto,
			@PathVariable int bankId) {

		return bankService.updateBank(bankUpdateDto, bankId);
	}

	// 6. get bank by pagination and sorting
	@GetMapping("/all/pagination")
	public ResponseEntity<ResponseStructure<List<Bank>>> getBanksByPaginationAndSorting(@RequestParam int pageNo,
			@RequestParam int pageSize,
			@RequestParam String sortBy, @RequestParam String sortDir) {

		return bankService.getBanksByPaginationAndSorting(pageNo, pageSize, sortBy, sortDir);
	}

	// 7. get by IFSC
	@GetMapping("/ifsc/{ifsc}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(@PathVariable String ifsc) {
		return bankService.getBankByIfsc(ifsc);
	}

	// 8. get by AddressId
	@GetMapping("/addressid/{addressId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(@PathVariable int addressId) {

		return bankService.getBankByAddressId(addressId);
	}

	// 9. get by address
	@GetMapping("/address")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddress(@RequestBody Address address) {
		return bankService.getBankByAddress(address);
	}

	// 10. get banks by city
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Bank>>> getByCity(@PathVariable String city) {

		return bankService.getByCity(city);

	}

	// 11. get bank by contactNumber

	@GetMapping("/contact/{contactNumber}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContact(@PathVariable long contactNumber) {
		return bankService.getBankByContact(contactNumber);

	}

}
