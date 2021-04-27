package com.meritamerica.assignment6.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CheckingAccount")
public class CheckingAccount extends BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
    private AccountHolder accountHolder;
	
	public CheckingAccount() {
		
	}
	public CheckingAccount(double balance) {
		super(balance,CHECKING_INTERESTRATE);
	}
	public CheckingAccount(long accountNumber, double balance, double interestRate,java.util.Date accountOpenedOn) {
		super(accountNumber,balance,interestRate,accountOpenedOn);
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
		
	public static final double CHECKING_INTERESTRATE= 0.0001;
}
