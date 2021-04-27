package com.meritamerica.assignment6.models;

import java.io.FileReader;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass

public abstract class BankAccount {

	private  double balance;
	private  double interestRate;
	private  long accountNumber;
	private  java.util.Date date;
	
	public BankAccount(){
	
	}
	public BankAccount(double balance, double interestRate) {
		this.balance=balance;
		this.interestRate = interestRate;
	}
	public BankAccount(double balance,double interestRate,java.util.Date accountOpenedOn) {
		this.balance=balance;
		this.interestRate = interestRate;
		this.date = accountOpenedOn;
	}
	public BankAccount(long accountNumber, double balance, double interestRate,java.util.Date accountOpenedOn) {
		this.accountNumber= accountNumber;
		this.balance=balance;
		this.interestRate = interestRate;
		this.date = accountOpenedOn;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public  java.util.Date getOpenedOn(){
		return this.date;
	}	
	public long getAccountNumber() {
		return this.accountNumber;
	}
	public double getBalance() {
		return this.balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getInterestRate() {
		return this.interestRate;
	}
	/*public boolean withdraw(double amount) {
		
		if( (balance-amount)>=0){
			balance-= amount;
			return true;
		}
		return false;
	}
	public boolean deposit (double amount) {
		if(amount>0) {
			this.balance += amount;
			return true;
		} else {
			return false;
		}
		
	}
	public double futureValue(int years) {
		double futureVal = balance * Math.pow((1+this.interestRate),years);
		return futureVal;
	}
	public double futureValue(int years) {
		double futureVal = MeritBankServiceImpl.recursiveFutureValue(this.getBalance(), years, this.getInterestRate());
		return futureVal;
	}
	public static BankAccount readFromString(String accountData) throws java.lang.NumberFormatException{
		StringTokenizer token = new StringTokenizer(accountData, ",");
		int numAccount = Integer.parseInt(token.nextToken());
		double balance = Double.parseDouble(token.nextToken());
		double rate = Double.parseDouble(token.nextToken());
		Date date = new Date(token.nextToken());//
		BankAccount bank = new BankAccount(numAccount, balance, rate, date);
		return bank;
	}

	public String writeToString() {
		String accountString = getAccountNumber()+","+getBalance()+","+getInterestRate()+","+getOpenedOn(); 
		return accountString;
	}
	public void addTransaction(Transaction transaction) {
		this.transactAmount.add(transaction);
	}
	
	public List<Transaction> getTransactions() {
		return this.transactAmount;
	}

	public String toString() {
		String toStr =  "Acct Num "+getAccountNumber()+ " balance "+getBalance();
		return toStr;
	}
	  	
	private ArrayList<Transaction> transactAmount = new ArrayList<Transaction>();*/
}
