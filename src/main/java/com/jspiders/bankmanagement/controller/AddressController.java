package com.jspiders.bankmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.exception.InvalidRequestException;
import com.jspiders.bankmanagement.repository.AddressRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	AddressRepository addressRepository;
	@Autowired
	BankRepository bankRepository;
	
	// get address by Id
	@GetMapping("/id/{addressId}")
	public ResponseEntity<ResponseStructure<Address>> getAddressById(@PathVariable int addressId){
		
		Optional<Address> opt = addressRepository.findById(addressId);
		
		if(opt.isPresent()) {
			ResponseStructure<Address> response = new ResponseStructure<Address>();
			response.setMessage("Address retrived associated with the given id");
			response.setData(opt.get());
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}else {
			throw new  AddressNotFoundException("This addressId is not assocatied with any address");
		}
	}
	
	@GetMapping("/bankid/{bankId}")
	public ResponseEntity<ResponseStructure<Address>> getByBank(@PathVariable int bankId){
		
		Optional<Bank> opt = bankRepository.findById(bankId);
		
		if(opt.isEmpty()) {
			throw new BankNotFoundException("This id is not associated with any bank");
		}else {
			ResponseStructure<Address> response = new ResponseStructure<>();
			
			response.setMessage("Address retrieved");
			response.setData(opt.get().getAddress());
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	}
	
	// get address by city
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Address>>> getByCity(@PathVariable String city){
		
		List<Address> addresses = addressRepository.findByCity(city);
		
		if(addresses.isEmpty()) {
			throw new AddressNotFoundException("No address exists in this city");
		}else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address retrieved");
			response.setData(addresses);
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	}
	
	// get address by city and street
	@GetMapping("/addressandcity")
	public ResponseEntity<ResponseStructure<List<Address>>> getByCityAndStreet(@RequestParam String city, @RequestParam String street){
		
		List<Address> addresses = addressRepository.findByCityAndStreet(city,street);
		
		if(addresses.isEmpty()) {
			throw new AddressNotFoundException("No address exists in this city");
		}else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address retrieved");
			response.setData(addresses);
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	}
	
	// get all existing address
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Address>>> getAllAddress(){
		
		List<Address> addresses = addressRepository.findAll();
		
		if(addresses.isEmpty()) {
			throw new AddressNotFoundException("Address does not exist");
		}else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address fetched successfully");
			response.setData(addresses);
			
			return new ResponseEntity<ResponseStructure<List<Address>>>(response,HttpStatus.OK);
		}
		
	}
	
	//update address
	@PutMapping("/{addressId}")
	ResponseEntity<ResponseStructure<Address>> updateAddress(@RequestBody AddressUpdateDto addressDto, @PathVariable int addressId){
		Optional<Address> opt = addressRepository.findById(addressId);
		
		if(opt.isEmpty()) {
			
			throw new AddressNotFoundException("Address associated with given id does not exist in database");
			
		}
		
		Address existingAddress = opt.get();
			
		existingAddress.setCity(addressDto.getCity());
		existingAddress.setStreet(addressDto.getStreet());
		existingAddress.setState(addressDto.getState());
		existingAddress.setPinCode(addressDto.getPinCode());
			
		addressRepository.save(existingAddress);
			
		ResponseStructure<Address> response = new ResponseStructure<>();
		response.setMessage("Address updated successfully");
		response.setData(existingAddress);
			
		return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		
	}
	
	//deletion
	@DeleteMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteById(@PathVariable int id){
		
		Optional<Address> opt = addressRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new AddressNotFoundException("Address with this id does not exist");
		}
		
		addressRepository.deleteById(opt.get().getAddressId());
		
		ResponseStructure<Void> response = new ResponseStructure<Void>();
		response.setMessage("Address deleted");
		response.setData(null);
		
		return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
}
