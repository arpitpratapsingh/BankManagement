package com.jspiders.bankmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankAlreadyExistsException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.repository.BankRepository;

@RequestMapping("/bank")
@RestController
public class BankController {
	@Autowired
	BankRepository bankRepository;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Bank>> createBank(@RequestBody Bank bank){
		
		if(bankRepository.existsByContactNumber(bank.getContactNumber())) {
			throw new BankAlreadyExistsException("Bank with this contact number already exists");
		}
		if(bankRepository.existsByIfsc(bank.getIfsc())) {
			throw new BankAlreadyExistsException("Bank with this contact IFSC already exists");
		}if(bank.getAddress()==null) {
			throw new AddressNotFoundException("Address field can not be empty");
		}
		else{
			bankRepository.save(bank);
			
			ResponseStructure<Bank> response = new ResponseStructure<>();
			response.setMessage("Bank created");
			response.setData(bank);
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		
	}
	
	// get bank by contactNumber
	
	@GetMapping("/contact/{contactNumber}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContact(@PathVariable long contactNumber){
	
		Optional<Bank> opt = bankRepository.findByContactNumber(contactNumber);
		
		if(opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();
			
			response.setMessage("Bank details retrieved successfully");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}else {
			throw new BankNotFoundException("This contact number is not associated with any bank");
		}
		
	}
	
	// get by id
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable int id){
		
		Optional<Bank> opt = bankRepository.findById(id);
		
		if(opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();
			
			response.setMessage("Bank retrieved");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else
			throw new BankNotFoundException("Bank with this id does not exist");
	}
	
	// get by IFSC
	
	@GetMapping("/ifsc/{ifsc}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(@PathVariable String ifsc){
		
		Optional<Bank> opt = bankRepository.findByIfsc(ifsc);
		
		if(opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();
			
			response.setMessage("Bank retrieved");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else
			throw new BankNotFoundException("Bank with this IFSC does not exist");
	}
	
	// get by AddressId
	
	@GetMapping("/addressid/{addressId}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(@PathVariable int addressId){
		
		Optional<Bank> opt = bankRepository.findByAddress_AddressId(addressId);
		
		if(opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();
			
			response.setMessage("Bank retrieved");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else
			throw new BankNotFoundException("Bank with the give address id does not exist");
	}
	
	
	//get by address
	
	@GetMapping("/address")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddress(@RequestBody Address address){
		
		Optional<Bank> opt = bankRepository.findByAddress(address);
		
		if(opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();
			
			response.setMessage("Bank retrieved");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else
			throw new BankNotFoundException("Bank with the give address does not exist");
	}
	
	// get banks by city
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Bank>>> getByCity(@PathVariable String city){
		
		List<Bank> banks = bankRepository.findByAddress_City(city);
		
		if(banks.isEmpty()) {
			throw new BankNotFoundException("No bank found in this city");
		}else{
			ResponseStructure<List<Bank>> response = new ResponseStructure<List<Bank>>();
			
			response.setMessage("Banks retrived");
			response.setData(banks);
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		
	}
	
	// get all banks
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank(){
		
		ResponseStructure<List<Bank>> response = new ResponseStructure<List<Bank>>();
		List<Bank> banks = bankRepository.findAll();
		
		if(banks.isEmpty()) {
			response.setMessage("No banks found");
			response.setData(banks);
		}else {
			response.setMessage("Banks retrieved");
			response.setData(banks);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
			
	}
	
	// delete using contact number
	@DeleteMapping("/{contactNumber}")
	public ResponseEntity<ResponseStructure<Void>> deleteByContactNumber(@PathVariable long contactNumber){
		Optional<Bank> opt = bankRepository.findByContactNumber(contactNumber);
		
		if(opt.isEmpty()) {
			throw new BankNotFoundException("Bank associated with this number does not exist");
		}else {
			bankRepository.deleteByContactNumber(contactNumber);
			ResponseStructure<Void> response = new ResponseStructure<Void>();
			
			response.setMessage("Bank deleted");
			response.setData(null);
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
