package com.meritamerica.assignment6.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;

import com.meritamerica.assignment6.services.MeritBankServiceImpl;

@Entity
@Table(name = "CDOffering")
public class CDOffering {
	/*
	 * public CDOffering(int term, double interestRate) { this.term = term;
	 * this.interestRate = interestRate;
	 * 
	 * }
	 */
	public CDOffering() {

	}

	private int term;
	private double interestRate;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private static int nextId = 1;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cdOffering")
	private List<CDAccount> cdAccounts;

	public List<CDAccount> getCdAccounts() {
		return cdAccounts;
	}

	public void setCdAccounts(List<CDAccount> cdAccounts) {
		this.cdAccounts = cdAccounts;
	}

	public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
		this.id = nextId++;
	}

	public int getTerm() {
		return this.term;
	}

	public double getInterestRate() {
		return this.interestRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
