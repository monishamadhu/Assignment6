package com.meritamerica.assignment5.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.meritamerica.assignment5.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment5.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment5.exceptions.NegativeAmountException;
import com.meritamerica.assignment5.exceptions.NoResourceFoundException;
import com.meritamerica.assignment5.models.AccountHolder;
import com.meritamerica.assignment5.models.CDAccount;
import com.meritamerica.assignment5.models.CDAccountDTO;
import com.meritamerica.assignment5.models.CDOffering;
import com.meritamerica.assignment5.models.CheckingAccount;
import com.meritamerica.assignment5.models.MeritBank;
import com.meritamerica.assignment5.models.SavingsAccount;


@RestController
public class BankController {
	
	/*@GetMapping(value="/greeting")
	public String greeting() {
		return "Hello";
	}*/
	@PostMapping(value="/CDOfferings")
	@ResponseStatus(HttpStatus.CREATED)
	public CDOffering cdOfferings(@RequestBody CDOffering cdoffering) throws InvalidAccountDetailsException {
		
		if((cdoffering.getTerm()==0)||cdoffering.getInterestRate()<=0||cdoffering.getTerm()<1||cdoffering.getInterestRate()>=1){
			throw new InvalidAccountDetailsException("Invalid details");
		}
		
		MeritBank.addCDOffering(cdoffering);
		return cdoffering;
	}
	
	@GetMapping(value="/CDOfferings")
	public List<CDOffering> getCDOfferings() {
		
		return MeritBank.getCDOffering();
	}
	
	@PostMapping(value="/AccountHolders")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccHolders(@RequestBody @Valid AccountHolder accountHolder) throws InvalidAccountDetailsException {
		if ((accountHolder.getFirstName() == null) || (accountHolder.getLastName() == null) ||(accountHolder.getSSN() == null)) {
			throw new InvalidAccountDetailsException("Invalid details");
		}
		MeritBank.addAccountHolder(accountHolder);
		return accountHolder;
	}
	
	@GetMapping(value="/AccountHolders")
	public AccountHolder[] getAccHolders() {
		return MeritBank.getAccountHolders();
	}
	
	@GetMapping(value="/AccountHolders/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder getAccHoldersById(@PathVariable int id, AccountHolder accountHolder) throws NoResourceFoundException{
		if (id<=MeritBank.accHolderList.size()) {
			if(accountHolder.getId()==id) {
				return MeritBank.accHolderList.get(id-1);
			}
		}
		 throw new NoResourceFoundException("Invalid id");
	} 
	
	@PostMapping(value="/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount addCheckingAcc(@RequestBody CheckingAccount ch, @PathVariable int id) throws NoResourceFoundException,ExceedsCombinedBalanceLimitException,NegativeAmountException {
			
		
		if (id<=MeritBank.accHolderList.size()) {
			AccountHolder accountHolder = MeritBank.getAccountHolderById(id);
			if(ch.getBalance()<0) {
				throw new NegativeAmountException();
			} 				
			if (accountHolder.getCombinedBalance()+ch.getBalance()>250000) {
				throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
			}
			ch=new CheckingAccount(MeritBank.getNextAccountNumber(),ch.getBalance(),CheckingAccount.CHECKING_INTERESTRATE,MeritBank.getDate());
			accountHolder.addCheckingAccount(ch);
			
			return ch;
		}
		throw new NoResourceFoundException("Invalid id");
	}
	
	@GetMapping(value="/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount[] getCheckingAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
		if(id>MeritBank.accHolderList.size()) {
			throw new NoResourceFoundException("Invalid id");
		}
		accountHolder = MeritBank.getAccountHolderById(id);		
		return accountHolder.getCheckingAccounts();
	}
	
	@PostMapping(value="/AccountHolders/{id}/SavingsAccounts") 
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount addSavingsAcc(@RequestBody SavingsAccount sv, @PathVariable int id) throws NoResourceFoundException,ExceedsCombinedBalanceLimitException,NegativeAmountException{
		if(id<=MeritBank.accHolderList.size()) {
			AccountHolder accountHolder = MeritBank.getAccountHolderById(id);
			if(sv.getBalance()<0) {
				throw new NegativeAmountException();
			} 
			if((accountHolder.getCombinedBalance() + sv.getBalance()) > 250000) {
					throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
				}
				sv=new SavingsAccount(MeritBank.getNextAccountNumber(),sv.getBalance(),SavingsAccount.SAVINGS_INTERESTRATE,MeritBank.getDate());
				accountHolder.addSavingsAccount(sv);

				return sv;
			}
		
		throw new NoResourceFoundException("Invalid id");
	}
	
	@GetMapping(value="/AccountHolders/{id}/SavingsAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount[] getSavingsAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
		if(id>MeritBank.accHolderList.size())
			throw new NoResourceFoundException("Invalid id");
		accountHolder = MeritBank.getAccountHolderById(id);
		return accountHolder.getSavingsAccounts();
	}
	
	@PostMapping(value="/AccountHolders/{id}/CDAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CDAccount addCDAccounts(@RequestBody CDAccountDTO dto,@PathVariable int id) throws NoResourceFoundException,NegativeAmountException {
		if(id<=MeritBank.accHolderList.size()) {
			CDOffering cdo = MeritBank.getCDOfferingById(dto.getCdOffering().getId());
			CDAccount cda = new CDAccount(cdo, dto.getBalance());
			
			AccountHolder accountHolder = MeritBank.getAccountHolderById(id);
			if(cda.getBalance()<0) {
				throw new NegativeAmountException();
			} else {
				accountHolder.addCDAccount(cda); 
				return cda;
			}
		}
		throw new NoResourceFoundException("Invalid id");
	}
	
}
