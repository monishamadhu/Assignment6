package com.meritamerica.assignment6.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.meritamerica.assignment6.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment6.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment6.exceptions.NegativeAmountException;
import com.meritamerica.assignment6.exceptions.NoResourceFoundException;
import com.meritamerica.assignment6.models.AccountHolder;
import com.meritamerica.assignment6.models.CDAccount;
import com.meritamerica.assignment6.models.CDAccountDTO;
import com.meritamerica.assignment6.models.CDOffering;
import com.meritamerica.assignment6.models.CheckingAccount;
import com.meritamerica.assignment6.models.SavingsAccount;
import com.meritamerica.assignment6.services.MeritBankServiceImpl;


@RestController
public class BankController {
	
	/*@GetMapping(value="/greeting")
	public String greeting() {
		return "Hello";
	}*/
	@Autowired
	MeritBankServiceImpl meritBankServiceImpl;
	@PostMapping(value="/CDOfferings")
	@ResponseStatus(HttpStatus.CREATED)
	public CDOffering cdOfferings(@RequestBody CDOffering cdoffering) throws InvalidAccountDetailsException {
		
		if((cdoffering.getTerm()==0)||cdoffering.getInterestRate()<=0||cdoffering.getTerm()<1||cdoffering.getInterestRate()>=1){
			throw new InvalidAccountDetailsException("Invalid details");
		}
		
		meritBankServiceImpl.addCDOffering(cdoffering);
		return cdoffering;
	}
	
	@GetMapping(value="/CDOfferings")
	public List<CDOffering> getCDOfferings() {
		
		return meritBankServiceImpl.getCDOffering();
	}
	
	@PostMapping(value="/AccountHolders")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccHolders(@RequestBody @Valid AccountHolder accountHolder) throws InvalidAccountDetailsException {
		if ((accountHolder.getFirstName() == null) || (accountHolder.getLastName() == null) ||(accountHolder.getSSN() == null)) {
			throw new InvalidAccountDetailsException("Invalid details");
		}
		meritBankServiceImpl.addAccountHolder(accountHolder);
		return accountHolder;
	}
	
	@GetMapping(value="/AccountHolders")
	public List<AccountHolder> getAccHolders() {
		return meritBankServiceImpl.getAccountHolders();
	}
	
	@GetMapping(value="/AccountHolders/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder getAccHoldersById(@PathVariable int id, AccountHolder accountHolder) throws NoResourceFoundException{
		if (id<=meritBankServiceImpl.accHolderList.size()) {
			if(accountHolder.getId()==id) {
				return meritBankServiceImpl.accHolderList.get(id-1);
			}
		}
		 throw new NoResourceFoundException("Invalid id");
	} 
	
	@PostMapping(value="/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount addCheckingAcc(@RequestBody CheckingAccount ch, @PathVariable int id) throws NoResourceFoundException,ExceedsCombinedBalanceLimitException,NegativeAmountException {
			
		
		if (id<=meritBankServiceImpl.accHolderList.size()) {
			AccountHolder accountHolder = meritBankServiceImpl.getAccountHolderById(id);
			if(ch.getBalance()<0) {
				throw new NegativeAmountException();
			} 				
			if (accountHolder.getCombinedBalance()+ch.getBalance()>250000) {
				throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
			}
			ch=new CheckingAccount(meritBankServiceImpl.getNextAccountNumber(),ch.getBalance(),CheckingAccount.CHECKING_INTERESTRATE,meritBankServiceImpl.getDate());
			accountHolder.addCheckingAccount(ch);
			
			return ch;
		}
		throw new NoResourceFoundException("Invalid id");
	}
	
	@GetMapping(value="/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public List<CheckingAccount> getCheckingAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
		if(id>meritBankServiceImpl.accHolderList.size()) {
			throw new NoResourceFoundException("Invalid id");
		}
		accountHolder = meritBankServiceImpl.getAccountHolderById(id);		
		return accountHolder.getCheckingAccountList();
	}
	
	@PostMapping(value="/AccountHolders/{id}/SavingsAccounts") 
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount addSavingsAcc(@RequestBody SavingsAccount sv, @PathVariable int id) throws NoResourceFoundException,ExceedsCombinedBalanceLimitException,NegativeAmountException{
		if(id<=meritBankServiceImpl.accHolderList.size()) {
			AccountHolder accountHolder = meritBankServiceImpl.getAccountHolderById(id);
			if(sv.getBalance()<0) {
				throw new NegativeAmountException();
			} 
			if((accountHolder.getCombinedBalance() + sv.getBalance()) > 250000) {
					throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
			}
			sv=new SavingsAccount(meritBankServiceImpl.getNextAccountNumber(),sv.getBalance(),SavingsAccount.SAVINGS_INTERESTRATE,meritBankServiceImpl.getDate());
			accountHolder.addSavingsAccount(sv);

			return sv;
		}
		
		throw new NoResourceFoundException("Invalid id");
	}
	
	@GetMapping(value="/AccountHolders/{id}/SavingsAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public List<SavingsAccount> getSavingsAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
		if(id>meritBankServiceImpl.accHolderList.size())
			throw new NoResourceFoundException("Invalid id");
		accountHolder = meritBankServiceImpl.getAccountHolderById(id);
		return accountHolder.getSavingsAccountList();
	}
	
	@PostMapping(value="/AccountHolders/{id}/CDAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CDAccount addCDAccounts(@RequestBody CDAccountDTO dto,@PathVariable int id) throws NoResourceFoundException,NegativeAmountException {
		if(id<=meritBankServiceImpl.accHolderList.size()) {
			
			AccountHolder accountHolder = meritBankServiceImpl.getAccountHolderById(id);
			CDOffering cdo = meritBankServiceImpl.getCDOfferingById(dto.getCdOffering().getId());
			CDAccount cda = new CDAccount(accountHolder.getTotalAccounts()+1, cdo,dto.getBalance());
			
			if(cda.getBalance()<0) {
				throw new NegativeAmountException();
			} 
			
			accountHolder.addCDAccount(cda); 
			return cda;
		}
		throw new NoResourceFoundException("Invalid id");
	}
	
	@GetMapping(value="/AccountHolders/{id}/CDAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CDAccount[] getCDAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
		if(id>meritBankServiceImpl.accHolderList.size())
			throw new NoResourceFoundException("Invalid id");
		accountHolder = meritBankServiceImpl.getAccountHolderById(id);
		return accountHolder.getCDAccounts();
	}
	
}
