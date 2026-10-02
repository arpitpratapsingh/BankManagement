package com.jspiders.bankmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspiders.bankmanagement.dto.BankUpdateDto;
import com.jspiders.bankmanagement.dto.ResponseStructure;
import com.jspiders.bankmanagement.entity.Account;
import com.jspiders.bankmanagement.entity.Address;
import com.jspiders.bankmanagement.entity.Bank;
import com.jspiders.bankmanagement.exception.AddressAlreadyExistsException;
import com.jspiders.bankmanagement.exception.AddressNotFoundException;
import com.jspiders.bankmanagement.exception.BankAlreadyExistsException;
import com.jspiders.bankmanagement.exception.BankNotFoundException;
import com.jspiders.bankmanagement.exception.InvalidRequestException;
import com.jspiders.bankmanagement.repository.AccountRepository;
import com.jspiders.bankmanagement.repository.AddressRepository;
import com.jspiders.bankmanagement.repository.BankRepository;

@Service
public class BankService {

	@Autowired
	BankRepository bankRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	AccountRepository accountRepository;

	// 1. create bank
	public ResponseEntity<ResponseStructure<Bank>> createBank(Bank bank) {

		// contact number uniqueness
		if (bankRepository.existsByContactNumber(bank.getContactNumber())) {
			throw new BankAlreadyExistsException("Bank with this contact number already exists");
		}
		// checking length of the contact number
		if (bank.getContactNumber() < 1000000000L || bank.getContactNumber() > 9999999999L) {
			throw new InvalidRequestException("Contact number must contain exactly 10 digits");
		}

		// IFSC uniqueness checking
		if (bankRepository.existsByIfsc(bank.getIfsc())) {
			throw new BankAlreadyExistsException("Bank with this contact IFSC already exists");
		}

		// address is not null checking
		if (bank.getAddress() == null) {
			throw new AddressNotFoundException("Address field can not be empty");
		}
		String pinCode = bank.getAddress().getPinCode();

		// PinCode digits checking
		if (pinCode == null || !pinCode.matches("\\d{6}")) {
			throw new InvalidRequestException(
					"Pincode must contain exactly 6 digits");
		}
		// PinCode duplication checking
		Optional<Address> existingAddress = addressRepository.findByPinCode(pinCode);

		if (existingAddress.isPresent()) {
			throw new AddressAlreadyExistsException(
					"An address with this pincode already exists");
		} else {
			bankRepository.save(bank);

			ResponseStructure<Bank> response = new ResponseStructure<>();
			response.setMessage("Bank created");
			response.setData(bank);

			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}

	}

	// 2. get all banks
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {

		ResponseStructure<List<Bank>> response = new ResponseStructure<List<Bank>>();
		List<Bank> banks = bankRepository.findAll();

		if (banks.isEmpty()) {
			response.setMessage("No banks found");
			response.setData(banks);
		} else {
			response.setMessage("Banks retrieved");
			response.setData(banks);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 3. get bank by id
	public ResponseEntity<ResponseStructure<Bank>> getBankById(int id) {

		Optional<Bank> opt = bankRepository.findById(id);

		if (opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();

			response.setMessage("Bank retrieved");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else
			throw new BankNotFoundException("Bank with this id does not exist");
	}

	// 4. delete bank
	public ResponseEntity<ResponseStructure<Void>> deleteByContactNumber(long contactNumber) {
		Optional<Bank> opt = bankRepository.findByContactNumber(contactNumber);

		if (opt.isEmpty()) {
			throw new BankNotFoundException("Bank associated with this number does not exist");
		}
		Bank bank = opt.get();

		List<Account> accounts = accountRepository.findByBankBankId(bank.getBankId());

		if (!accounts.isEmpty()) {
			throw new InvalidRequestException("Bank cannot be deleted becuase accounts are associated with it");
		}
		bankRepository.delete(bank);

		ResponseStructure<Void> response = new ResponseStructure<Void>();
		response.setMessage("Bank deleted");
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// 5. update bank
	public ResponseEntity<ResponseStructure<Bank>> updateBank(BankUpdateDto bankUpdateDto, int bankId) {

		Optional<Bank> opt = bankRepository.findById(bankId);

		if (opt.isEmpty()) {
			throw new BankNotFoundException("Bank assocated with this id does not exist");
		}

		Bank existingBank = opt.get();

		// checking the length of contact number
		if (bankUpdateDto.getContactNumber() < 1000000000L || bankUpdateDto.getContactNumber() > 9999999999L) {
			throw new InvalidRequestException("Contact number must contain exactly 10 digits");
		}

		// checking duplicate contact number
		Optional<Bank> duplicateByContactNumber = bankRepository.findByContactNumber(bankUpdateDto.getContactNumber());
		if (duplicateByContactNumber.isPresent() && duplicateByContactNumber.get().getBankId() != bankId) {
			throw new BankAlreadyExistsException("One bank with this phone number already exists");
		}

		Optional<Bank> duplicateByIfsc = bankRepository.findByIfsc(bankUpdateDto.getIfsc());

		if (duplicateByIfsc.isPresent() && duplicateByIfsc.get().getBankId() != bankId) {
			throw new BankAlreadyExistsException("One bank with this IFSC already exists");
		}

		existingBank.setBankName(bankUpdateDto.getBankName());
		existingBank.setContactNumber(bankUpdateDto.getContactNumber());
		existingBank.setIfsc(bankUpdateDto.getIfsc());
		existingBank.setBranchName(bankUpdateDto.getBranchName());

		bankRepository.save(existingBank);

		ResponseStructure<Bank> response = new ResponseStructure<Bank>();
		response.setMessage("Bank updated successfully");
		response.setData(existingBank);

		return new ResponseEntity<ResponseStructure<Bank>>(response, HttpStatus.OK);
	}

	// 6. get bank by pagination and sorting
	public ResponseEntity<ResponseStructure<List<Bank>>> getBanksByPaginationAndSorting(int pageNo, int pageSize,
			String sortBy, String sortDir) {

		Sort sort = null;
		if (sortDir.equalsIgnoreCase("ASC")) {
			sort = Sort.by(sortBy).ascending();
		} else if (sortDir.equalsIgnoreCase("DESC")) {
			sort = Sort.by(sortBy).descending();
		} else {
			throw new InvalidRequestException("Sort direction must be ASC or DESC");
		}
		List<Bank> banks = bankRepository.findAll(PageRequest.of(pageNo, pageSize, sort)).getContent();

		if (banks.isEmpty()) {
			throw new BankNotFoundException("Bank does not exist");
		}

		ResponseStructure<List<Bank>> response = new ResponseStructure<>();

		response.setMessage("Bank retrieved");
		response.setData(banks);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 7. get by IFSC
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(String ifsc) {

		Optional<Bank> opt = bankRepository.findByIfsc(ifsc);

		if (opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();

			response.setMessage("Bank retrieved");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else
			throw new BankNotFoundException("Bank with this IFSC does not exist");
	}

	// 8. get by AddressId
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddressId(int addressId) {

		Optional<Bank> opt = bankRepository.findByAddress_AddressId(addressId);

		if (opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();

			response.setMessage("Bank retrieved");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else
			throw new BankNotFoundException("Bank with the give address id does not exist");
	}

	// 9. get by address
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddress(Address address) {

		Optional<Bank> opt = bankRepository.findByAddress(address);

		if (opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();

			response.setMessage("Bank retrieved");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else
			throw new BankNotFoundException("Bank with the give address does not exist");
	}

	// 10. get banks by city
	public ResponseEntity<ResponseStructure<List<Bank>>> getByCity(String city) {

		List<Bank> banks = bankRepository.findByAddress_City(city);

		if (banks.isEmpty()) {
			throw new BankNotFoundException("No bank found in this city");
		} else {
			ResponseStructure<List<Bank>> response = new ResponseStructure<List<Bank>>();

			response.setMessage("Banks retrived");
			response.setData(banks);

			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

	// 11. get bank by contactNumber

	public ResponseEntity<ResponseStructure<Bank>> getBankByContact(long contactNumber) {

		Optional<Bank> opt = bankRepository.findByContactNumber(contactNumber);

		if (opt.isPresent()) {
			ResponseStructure<Bank> response = new ResponseStructure<Bank>();

			response.setMessage("Bank details retrieved successfully");
			response.setData(opt.get());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new BankNotFoundException("This contact number is not associated with any bank");
		}

	}

}
