package com.jspiders.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.bankmanagement.dto.AddressUpdateDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.exception.AddressAlreadyExistsException;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.exception.InvalidRequestException;
import com.jspiders.bankmanagement.repository.AddressRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	BankRepository bankRepository;

	// 1. get address by Id
	public ResponseEntity<ResponseStructure<Address>> getAddressById(int addressId) {

		Optional<Address> opt = addressRepository.findById(addressId);

		if (opt.isPresent()) {
			ResponseStructure<Address> response = new ResponseStructure<Address>();
			response.setMessage("Address retrived associated with the given id");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new AddressNotFoundException("This addressId is not assocatied with any address");
		}
	}

	// 2. update address
	public ResponseEntity<ResponseStructure<Address>> updateAddress(AddressUpdateDto addressDto, int addressId) {
		Optional<Address> opt = addressRepository.findById(addressId);

		if (opt.isEmpty()) {
			throw new AddressNotFoundException("Address associated with given id does not exist in database");
		}

		if (addressDto.getCity() == null || addressDto.getCity().trim().isEmpty()) {
			throw new InvalidRequestException("City cannot be empty");
		}

		if (addressDto.getStreet() == null || addressDto.getStreet().trim().isEmpty()) {
			throw new InvalidRequestException("Street cannot be empty");
		}

		if (addressDto.getState() == null || addressDto.getState().trim().isEmpty()) {
			throw new InvalidRequestException("State cannot be empty");
		}

		Address existingAddress = opt.get();

		// checking for pincode 6 digits

		String pinCode = addressDto.getPinCode();
		if (pinCode == null || !pinCode.matches("\\d{6}")) {
			throw new InvalidRequestException("Pincode must contain exactly 6 digits");
		}

		// check pincode uniqueness
		Optional<Address> duplicateAddress = addressRepository.findByPinCode(addressDto.getPinCode());

		if (duplicateAddress.isPresent() && duplicateAddress.get().getAddressId() != addressId) {
			throw new AddressAlreadyExistsException("An address with this pincode already exists");
		}

		existingAddress.setCity(addressDto.getCity().trim());
		existingAddress.setStreet(addressDto.getStreet().trim());
		existingAddress.setState(addressDto.getState().trim());
		existingAddress.setPinCode(addressDto.getPinCode().trim());

		addressRepository.save(existingAddress);

		ResponseStructure<Address> response = new ResponseStructure<>();
		response.setMessage("Address updated successfully");
		response.setData(existingAddress);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 3. get address by bank
	public ResponseEntity<ResponseStructure<Address>> getByBank(int bankId) {

		Optional<Bank> opt = bankRepository.findById(bankId);
		if (opt.isEmpty()) {
			throw new BankNotFoundException("This id is not associated with any bank");
		} else {
			ResponseStructure<Address> response = new ResponseStructure<>();

			response.setMessage("Address retrieved");
			response.setData(opt.get().getAddress());

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	// 4. get address by city
	public ResponseEntity<ResponseStructure<List<Address>>> getByCity(String city) {

		List<Address> addresses = addressRepository.findByCity(city);

		if (addresses.isEmpty()) {
			throw new AddressNotFoundException("No address exists in this city");
		} else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address retrieved");
			response.setData(addresses);

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	// 5. get address by city and street

	public ResponseEntity<ResponseStructure<List<Address>>> getByCityAndStreet(String city, String street) {

		List<Address> addresses = addressRepository.findByCityAndStreet(city, street);

		if (addresses.isEmpty()) {
			throw new AddressNotFoundException("No address exists in this city");
		} else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address retrieved");
			response.setData(addresses);

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	// 6. get all existing address
	public ResponseEntity<ResponseStructure<List<Address>>> getAllAddress() {

		List<Address> addresses = addressRepository.findAll();

		if (addresses.isEmpty()) {
			throw new AddressNotFoundException("Address does not exist");
		} else {
			ResponseStructure<List<Address>> response = new ResponseStructure<List<Address>>();
			response.setMessage("Address fetched successfully");
			response.setData(addresses);

			return new ResponseEntity<ResponseStructure<List<Address>>>(response, HttpStatus.OK);
		}

	}

}
