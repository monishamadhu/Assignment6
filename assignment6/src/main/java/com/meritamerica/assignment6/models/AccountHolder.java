package com.meritamerica.assignment6.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "AccountHolder")
public class AccountHolder implements Comparable<AccountHolder> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	private String middleName;
	@NotBlank(message = "Last Name is mandatory")
	private String lastName;
	@NotBlank(message = "SSN is mandatory")
	private String ssn;
	private static int nextId = 1;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private AccountHoldersContactDetails accountHolderContactDetails;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<CheckingAccount> checkingAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<SavingsAccount> savingsAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<CDAccount> cdAccList = new ArrayList<>();
	
	public AccountHolder(String firstName, String middleName, String lastName, String ssn, int id) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.id = nextId++;
	}

	public AccountHolder() {

	}

	public List<CDAccount> getcdAccList() {
		return cdAccList;
	}

	public void setcdAccList(List<CDAccount> cdAccList) {
		this.cdAccList = cdAccList;
	}

	public List<SavingsAccount> getSavingsAccountList() {
		return savingsAccountList;
	}

	public void setSavingsAccountList(List<SavingsAccount> savingsAccList) {
		this.savingsAccountList = savingsAccList;
	}

	public AccountHoldersContactDetails getAccountHolderContactDetails() {
		return accountHolderContactDetails;
	}

	public void setAccountHolderContactDetails(AccountHoldersContactDetails accountHolderContactDetails) {
		this.accountHolderContactDetails = accountHolderContactDetails;
	}

	public List<CheckingAccount> getCheckingAccountList() {
		return checkingAccountList;
	}

	public void setCheckingAccountList(List<CheckingAccount> checkingAccList) {
		this.checkingAccountList = checkingAccList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSSN() {
		return ssn;
	}

	public void setSSN(String ssn) {
		this.ssn = ssn;
	}

	public CheckingAccount addCheckingAccount(double openingBalance) {
		CheckingAccount checking = null;
		if ((getSavingsBalance() + getCheckingBalance() + openingBalance) < 250000) {
			checking = new CheckingAccount(openingBalance);
			this.checkingAccountList.add(checking);
		}
		return checking;
	}

	public CheckingAccount addCheckingAccount(CheckingAccount checkingAccount) {
		if ((getSavingsBalance() + getCheckingBalance() + checkingAccount.getBalance()) < 250000) {
			this.checkingAccountList.add(checkingAccount);
			return checkingAccount;
		} else {
			return null;
		}
	}

	/*
	 * public CheckingAccount[] getCheckingAccounts() { CheckingAccount[] checking =
	 * checkingAccList.toArray(new CheckingAccount[0]); return checking; }
	 */

	public CheckingAccount getCheckingAccountById(int accountHolderId) {

		return checkingAccountList.get(accountHolderId - 1);
	}

	public int getNumberOfCheckingAccounts() {
		int numberOfCheckingAccounts = checkingAccountList.size();
		return numberOfCheckingAccounts;
	}

	public double getCheckingBalance() {
		CheckingAccount[] checkingArr = checkingAccountList.toArray(new CheckingAccount[0]);
		double checkingTotal = 0;
		for (int i = 0; i < checkingArr.length; i++) {
			checkingTotal += checkingArr[i].getBalance();
		}

		return checkingTotal;
	}

	public SavingsAccount addSavingsAccount(double openingBalance) {
		SavingsAccount savings = null;
		if ((getSavingsBalance() + getCheckingBalance() + openingBalance) < 250000) {
			savings = new SavingsAccount(openingBalance);
			this.savingsAccountList.add(savings);
		}
		return savings;
	}

	public SavingsAccount addSavingsAccount(SavingsAccount savingsAccount) {
		if ((getSavingsBalance() + getCheckingBalance() + savingsAccount.getBalance()) < 250000) {
			this.savingsAccountList.add(savingsAccount);
			return savingsAccount; 
		} else {
			return null;
		}
	}

	/*
	 * public SavingsAccount[] getSavingsAccounts() { SavingsAccount[] savings =
	 * savingsAccList.toArray(new SavingsAccount[0]); // converting to array since,
	 * return // type, an array is expected. return savings; }
	 */

	// calculates the length of the savings account array, getSavingsAccounts() AND
	// returns the array of savings account when invoked
	public int getNumberOfSavingsAccounts() {
		int numberOfSavingsAccount = savingsAccountList.size();
		return numberOfSavingsAccount;
	}

	public double getSavingsBalance() {
//		SavingsAccount[] savingArr = (SavingsAccount[]) savingsAccountList.toArray();
		double savingsTotal = 0;
		for (SavingsAccount sa : savingsAccountList) {
			savingsTotal += (sa.getBalance());
		}
		
//		for (int i = 0; i < savingArr.length; i++) {
//			savingsTotal += (savingArr[i].getBalance());
//		}

		return savingsTotal;
	}

	/*
	 * public CDAccount addCDAccount(CDOffering offering, double openingBalance) {
	 * 
	 * CDAccount cd = new CDAccount(offering, openingBalance);
	 * this.cdAccList.add(cd); return cd; }
	 */

	public CDAccount addCDAccount(CDAccount cdAccount) {
		this.cdAccList.add(cdAccount);
		return cdAccount;
	}

	public CDAccount[] getCDAccounts() {
		CDAccount[] cd = cdAccList.toArray(new CDAccount[0]);
		return cd;
	}

	public int getNumberOfCDAccounts() {
		int numberOfCDAccounts = cdAccList.size();
		return numberOfCDAccounts;
	}

	public double getCDBalance() {
		double cdTotal = 0;
		CDAccount[] cdArr = getCDAccounts();
		for (int i = 0; i < cdArr.length; i++) {
			cdTotal += cdArr[i].getBalance();
		}
		System.out.println("cd:" + cdTotal);
		return cdTotal;
	}

	public double getCombinedBalance() {
		return (getCheckingBalance() + getSavingsBalance()  + getCDBalance());
	}

	public int compareTo(AccountHolder ac) {
		if (this.getCombinedBalance() > ac.getCombinedBalance()) {
			return 1;
		} else {
			return -1;
		}
	}
	 public int getTotalAccounts() {
		 return getNumberOfCDAccounts()+getNumberOfSavingsAccounts()+getNumberOfCheckingAccounts();
	 }

}
