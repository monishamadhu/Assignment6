package com.meritamerica.assignment6.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment6.services.MeritBankService;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritamerica.assignment6.services.MeritBankServiceImpl;

@Entity
@Table(name="CDAccount")
public class CDAccount extends BankAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cdoffering_id")
	private  CDOffering cdOffering;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private  AccountHolder accountHolder;
	
	public CDAccount() {
		
	}
	public CDAccount(int nextAccountNumber, CDOffering cdOffering,  double balance) {
		
		super(nextAccountNumber, balance,cdOffering.getInterestRate(),new Date());
		
		this.cdOffering=cdOffering;
	}
	public CDAccount(long accountNumber, double balance, double interestRate,java.util.Date accountOpenedOn,int term) {
		super(accountNumber,balance,interestRate,accountOpenedOn);
		
		this.cdOffering = new CDOffering(term, interestRate);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
		
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public CDOffering getCdOffering() {
		return cdOffering;
	}
	
	public void setCdOffering(CDOffering cdOffering) {
		this.cdOffering = cdOffering;
	}

	public double getInterestRate() {
		return this.cdOffering.getInterestRate();
	}
	//@JsonIgnore
	public int getTerm() {
		return this.cdOffering.getTerm();
	}
	
	//@JsonIgnore
	public int getcdOfferingId() {
		return this.cdOffering.getId();
	}
	//@JsonIgnore
	public java.util.Date getStartDate(){
		Date date=new Date();
		return date;
	}
	/*public boolean withdraw(double amount) {
		return false;
	}
	public boolean deposit(double amount) {
		return false;
	}
	public double futureValue() {
		double futureVal = getBalance() * Math.pow((1+this.getInterestRate()),this.getTerm());
		return futureVal;
	}*/
	
}
