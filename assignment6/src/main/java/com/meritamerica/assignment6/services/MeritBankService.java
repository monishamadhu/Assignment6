package com.meritamerica.assignment6.services;

import java.util.Date;
import java.util.List;

import com.meritamerica.assignment6.models.AccountHolder;
import com.meritamerica.assignment6.models.CDOffering;

public interface MeritBankService {
	public void addAccountHolder(AccountHolder accountHolder);
	public List<AccountHolder> getAccountHolders();
	public AccountHolder getAccountHolderById(int accountHolderId);
	public List<CDOffering> getCDOfferings();
	public CDOffering getCDOfferingById(int cdOfferingId);
	//public CDOffering getBestCDOffering(double depositAmount);
	//public CDOffering getSecondBestCDOffering(double depositAmount);
	public void clearCDOfferings();
	public void setCDOfferings(CDOffering offerings);
	public long getNextAccountNumber();
	public Date getDate();
	public double totalBalances();
	public double futureValue(double presentValue, double interestRate, int term);
	public void setNextAccountNumber(long nextAccountNumber);
	//public boolean readFromFile(String fileName);
	public List<AccountHolder> sortAccountHolders();
	public List<CDOffering> getCDOffering();
	public void addCDOffering(CDOffering cdo);
	void setCDOfferings(List<CDOffering> offerings);
}
