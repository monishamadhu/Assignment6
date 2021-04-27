package com.meritamerica.assignment6.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment6.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment6.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment6.exceptions.NegativeAmountException;
import com.meritamerica.assignment6.exceptions.NoResourceFoundException;
import com.meritamerica.assignment6.models.AccountHolder;
import com.meritamerica.assignment6.models.AccountHoldersContactDetails;
import com.meritamerica.assignment6.models.CDAccount;
import com.meritamerica.assignment6.models.CDAccountDTO;
import com.meritamerica.assignment6.models.CDOffering;
import com.meritamerica.assignment6.models.CheckingAccount;
import com.meritamerica.assignment6.models.SavingsAccount;
import com.meritamerica.assignment6.repository.AccountHolderContactDetailsRepository;
import com.meritamerica.assignment6.repository.AccountHolderRepository;
import com.meritamerica.assignment6.repository.CDAccountRepository;
import com.meritamerica.assignment6.repository.CDOfferingRepository;
import com.meritamerica.assignment6.repository.CheckingAccountRepository;
import com.meritamerica.assignment6.repository.SavingsAccountRepository;

import com.meritamerica.assignment6.services.MeritBankServiceImpl;

@RestController
@RequestMapping("/rest")
public class AccountController {
	
	@Autowired
	CDOfferingRepository cdOfferingRepository; 
	
	@PostMapping(value="/cdofferings")
	@ResponseStatus(HttpStatus.CREATED)
	public CDOffering cdOfferings(@RequestBody CDOffering cdoffering) throws InvalidAccountDetailsException{
		if((cdoffering.getTerm()==0)||cdoffering.getInterestRate()<=0||cdoffering.getTerm()<1||cdoffering.getInterestRate()>=1){
			throw new InvalidAccountDetailsException("Invalid details");
		}
		cdOfferingRepository.save(cdoffering);
		return cdoffering;
	}
	
	@GetMapping(value="/cdofferings")
	public List<CDOffering> getCDOfferings() {
		return cdOfferingRepository.findAll();
	}
	//-----------------------------------------------------------------
	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	MeritBankServiceImpl meritBankServiceImpl;

	@PostMapping("/accountholder")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccHolders(@RequestBody @Valid AccountHolder accountHolder) throws InvalidAccountDetailsException {
		if ((accountHolder.getFirstName() == null) || (accountHolder.getLastName() == null) ||(accountHolder.getSSN() == null)) {
			throw new InvalidAccountDetailsException("Invalid details");
		}
		accountHolderRepository.save(accountHolder); 
		return accountHolder;
		//return accountHolderRepository.save(accountHolder); 
		//accountHolder = accountHolderRepository.save(accountHolder);
		//return accountHolderRepository.findById(a).orElse(null);
		
	}

	@GetMapping("/accountholders")
	public List<AccountHolder> getAll() {
	
		return accountHolderRepository.findAll();
	}
	
	@GetMapping(value="/accountholder/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder getAccHoldersById(@PathVariable int id, AccountHolder acch) throws NoResourceFoundException{
		acch = accountHolderRepository.findById(id).orElse(null);
		if (acch == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return acch;
	}

	@Autowired
	AccountHolderContactDetailsRepository accHolderContactDetailsRepository;

	@PostMapping("/accountholder/{id}/contactdetails")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccHoldersContact(@RequestBody @Valid AccountHoldersContactDetails accountHolderContact,@PathVariable int id) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.getOne(id);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		AccountHoldersContactDetails ahcontact=new AccountHoldersContactDetails(accountHolderContact.getPhoneNum(),accountHolderContact.getEmail());
		ahcontact.setAccountHolder(accountHolder);
		accHolderContactDetailsRepository.save(ahcontact);
		return ahcontact.getAccountHolder();
		//return accHolderContactDetailsRepository.save(accountHolderContact);
		
	}

	@GetMapping("/accountholder/{id}/contactdetails")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHoldersContactDetails getContactsAll(AccountHolder acch, @PathVariable int id) throws NoResourceFoundException{
		acch = accountHolderRepository.findById(id).orElse(null);
		if (acch == null) {
			throw new NoResourceFoundException("Invalid id");
		}	
		return acch.getAccountHolderContactDetails();
		
	}
//--------------------------------------------------------------------------------------------------------------------------------------
	@Autowired
	CheckingAccountRepository checkingAccountRepository;

	@PostMapping("/accountholder/{id}/checkingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount addCheckingAccount(@PathVariable int id, @RequestBody CheckingAccount checkingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException {

		if(checkingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.getOne(id);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if (accountHolder.getCombinedBalance()+checkingAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
		CheckingAccount ch = new CheckingAccount(meritBankServiceImpl.getNextAccountNumber(),checkingAccount.getBalance(), CheckingAccount.CHECKING_INTERESTRATE, meritBankServiceImpl.getDate());
		ch.setAccountHolder(accountHolder);
		return checkingAccountRepository.save(ch);
		 
	}

	@GetMapping("/accountholder/{id}/checkingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public List<CheckingAccount> getCheckingAcc(AccountHolder acch, @PathVariable int id) throws NoResourceFoundException {
		acch = accountHolderRepository.findById(id).orElse(null);
		if (acch == null) {
			throw new NoResourceFoundException("Invalid id");
		}	
		return acch.getCheckingAccountList();
	}

	@Autowired
	SavingsAccountRepository savingsAccountRepository;

	@PostMapping("/accountholder/{id}/savingsaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount addSavingsAccount(@PathVariable int id, @RequestBody SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException {
		if(savingsAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		AccountHolder accountHolder = accountHolderRepository.getOne(id);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if (accountHolder.getCombinedBalance()+savingsAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
		SavingsAccount sv = new SavingsAccount(meritBankServiceImpl.getNextAccountNumber(),
				savingsAccount.getBalance(), SavingsAccount.SAVINGS_INTERESTRATE, meritBankServiceImpl.getDate());
		sv.setAccountHolder(accountHolder);
		return savingsAccountRepository.save(sv);

	}

	@GetMapping("/accountholder/{id}/savingsaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public List<SavingsAccount> getSavingsAcc(AccountHolder acch, @PathVariable int id)
			throws NoResourceFoundException {
		
		acch = accountHolderRepository.findById(id).orElse(null);
		if (acch == null) {
			throw new NoResourceFoundException("Invalid id");
		}	
		return acch.getSavingsAccountList();
		

	}
	//----------------------------------------------------------------------------------------------------------------------
	@Autowired
	CDAccountRepository cdAccountRepository;

	@PostMapping("/accountholder/{id}/cdaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CDAccount addCDAccount(@PathVariable int id, @RequestBody CDAccountDTO dto) throws NoResourceFoundException, NegativeAmountException {
		if(dto.getBalance()<0) {
			throw new NegativeAmountException();
		}
		AccountHolder accountHolder = accountHolderRepository.getOne(id);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		CDOffering cdOffer= meritBankServiceImpl.getCDOfferingById(dto.getCdOffering().getId());
		CDAccount cd = new CDAccount(accountHolder.getTotalAccounts() + 1, cdOffer,dto.getBalance());
		cd.setAccountHolder(accountHolder);
		return cdAccountRepository.save(cd);

	}

	@GetMapping(value="/accountholder/{id}/cdaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public List<CDAccount> getCDAcc(AccountHolder accountHolder, @PathVariable int id) throws NoResourceFoundException {
	    accountHolder = accountHolderRepository.getOne(id);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getcdAccList();
	}

	

}
