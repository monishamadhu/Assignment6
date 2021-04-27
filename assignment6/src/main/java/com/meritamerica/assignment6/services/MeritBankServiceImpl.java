package com.meritamerica.assignment6.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment6.models.AccountHolder;
import com.meritamerica.assignment6.models.CDAccount;
import com.meritamerica.assignment6.models.CDOffering;
import com.meritamerica.assignment6.models.CheckingAccount;
import com.meritamerica.assignment6.models.SavingsAccount;
import com.meritamerica.assignment6.repository.AccountHolderRepository;
import com.meritamerica.assignment6.repository.CDOfferingRepository;
@Service
public class MeritBankServiceImpl implements MeritBankService {
	@Autowired
	CDOfferingRepository cdOfferingRepository; 
	@Autowired
	AccountHolderRepository accountHolderRepository;
	
	@Override
	public void addAccountHolder(AccountHolder accountHolder) {
		accHolderList.add(accountHolder);
	}
	
	@Override
	public List<AccountHolder> getAccountHolders() {
		//AccountHolder[] accHolderArr = accHolderList.toArray(new AccountHolder[0]);
		return accHolderList;
	}
	
	@Override
	public AccountHolder getAccountHolderById(int accountHolderId) {
		return accountHolderRepository.findById(accountHolderId).orElse(null);
		//return accHolderList.get(accountHolderId - 1);
	}
	
	
	public List<CDOffering> getCDOfferings() {
		return offerings;
	}
	
	@Override
	public CDOffering getCDOfferingById(int cdOfferingId) {
		
		return cdOfferingRepository.findById(cdOfferingId).orElse(null);
//		return offeringsList.get(cdOfferingId - 1);
	}
	
	/*@Override
	public CDOffering getBestCDOffering(double depositAmount) {
		// CDOffering[] offering= getCDOfferings();
		CDOffering bestCDOffer = offerings[0];
		for (int i = 1; i < offerings.length; i++) {
			double futureVal = futureValue(depositAmount, offerings[i].getInterestRate(), offerings[i].getTerm());
			double bestFutureVal = futureValue(depositAmount, bestCDOffer.getInterestRate(), bestCDOffer.getTerm());
			if (futureVal > bestFutureVal) {
				bestFutureVal = futureVal;
				bestCDOffer = offerings[i];
			}
		}
		return bestCDOffer;
	}*/
	
	/*@Override
	public CDOffering getSecondBestCDOffering(double depositAmount) {

		CDOffering secondBestOffer = null;
		for (int i = 1; i < offerings.length; i++) {
			for (int j = i + 1; j < offerings.length; j++) {
				double bestFutureVal = futureValue(depositAmount, offerings[i].getInterestRate(),
						offerings[i].getTerm());
				double futureVal = futureValue(depositAmount, offerings[j].getInterestRate(), offerings[j].getTerm());
				// double secondBestFutureVal =
				// futureValue(depositAmount,secondBestOffer.getInterestRate(),secondBestOffer.getTerm());
				CDOffering[] temp = new CDOffering[1];
				if (futureVal > bestFutureVal) {
					temp[0] = offerings[i];
					offerings[i] = offerings[j];
					offerings[j] = temp[0];
				}
			}
		}
		secondBestOffer = offerings[1];
		return secondBestOffer;
	}*/
	
	@Override
	public void clearCDOfferings() {
		offerings = null;
	}
	
	
	@Override
	public void setCDOfferings(List<CDOffering> offerings) {
		this.offerings = offerings; 
	}
	
	@Override
	public long getNextAccountNumber() {
		return nextAccNumber++;
	}

	@Override
	public Date getDate() {
		Date date = new Date();
		Format f = new SimpleDateFormat("dd/MM/yy");
		String strDate = f.format(date);
		date = new Date(strDate);
		return date;
	}
	
	@Override
	public double totalBalances() {
		List<AccountHolder> accountHolderArr = getAccountHolders();
		double totalBalance = 0;
		for (int i = 0; i < accountHolderArr.size(); i++) {
			totalBalance += accountHolderArr.get(i).getSavingsBalance() + accountHolderArr.get(i).getCheckingBalance()
					+ accountHolderArr.get(i).getCDBalance();
		}
		return totalBalance;
	}
	
	@Override
	public double futureValue(double presentValue, double interestRate, int term) {
		double futureVal = presentValue * Math.pow((1 + interestRate), term);
		return futureVal;
	}

	@Override
	public void setNextAccountNumber(long nextAccountNumber) {
		nextAccNumber = nextAccountNumber;
	}
	
	/*@Override
	public boolean readFromFile(String fileName) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(fileName));
			String line = rd.readLine(); // reads the first line
			setNextAccountNumber(Long.parseLong(line));
			line = rd.readLine(); // reads the 2nd line
			int cdLength = Integer.parseInt(line);
			CDOffering[] cd = new CDOffering[cdLength]; // makes an array for CDOffering
			for (int i = 0; i < cdLength; i++) { // length of array was read from 2nd line
				line = rd.readLine();
				// cd[i]=CDOffering.readFromString(line); //stores the CDOffering with (term and
				// rate) in each index
			}
			setCDOfferings(cd); // requirement:"when reading from file, the data should overwrite the
								// MeritBankServiceImpl data such that previous data no longer exists, only the
								// data read from the file should exist"
			line = rd.readLine();
			int accHolderLength = Integer.parseInt(line);
			accHolderList = new ArrayList<AccountHolder>();// the accHolderList now points to the newly created
															// arrayList of accounts which is read from the
															// file.//automatically the pointer has now changed from
															// previous lists to the new arrayList.
			for (int i = 0; i < accHolderLength; i++) {
				line = rd.readLine();
				// System.out.println("AH Read"+line);
				AccountHolder a = AccountHolder.readFromString(line);

				accHolderList.add(a);
				line = rd.readLine();
				int numOfChecking = Integer.parseInt(line);
				if (numOfChecking != 0) {
					for (int j = 0; j < numOfChecking; j++) {

						line = rd.readLine();
						// System.out.println("CA Read"+line);
						CheckingAccount ch = CheckingAccount.readFromString(line);
						a.addCheckingAccount(ch);
					}
				}
				line = rd.readLine();
				int numOfSavings = Integer.parseInt(line);
				if (numOfSavings != 0) {
					for (int j = 0; j < numOfSavings; j++) {

						line = rd.readLine();
						// System.out.println("SA Read"+line);
						SavingsAccount sv = SavingsAccount.readFromString(line);
						a.addSavingsAccount(sv);
					}
				}
				line = rd.readLine();
				int numOfCD = Integer.parseInt(line);
				if (numOfCD != 0) {
					for (int j = 0; j < numOfCD; j++) {

						line = rd.readLine();
						//CDAccount cdAcc = CDAccount.readFromString(line);
						//a.addCDAccount(cdAcc);

						// a.addCDAccount(offerings[j], balance);
					}
				}
			}
			rd.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * public static boolean writeToFile(String fileName) { try{ PrintWriter wr =
	 * new PrintWriter(new FileWriter(fileName)); //wr.print("");
	 * wr.println(nextAccNumber); wr.println(offerings.length); for(int
	 * i=0;i<offerings.length;i++) { wr.println(offerings[i].writeToString()); }
	 * wr.println(accHolderList.size()); for(int i=0;i<accHolderList.size();i++) {
	 * AccountHolder accInfo=accHolderList.get(i);
	 * wr.println(accInfo.writeToString()); int numOfCheckings =
	 * accInfo.getNumberOfCheckingAccounts(); wr.println(numOfCheckings);
	 * if(numOfCheckings!=0) { // CheckingAccount[]
	 * checking=accInfo.getCheckingAccount().toArray(CheckingAccount[0]); for(int
	 * j=0;j<numOfCheckings;j++) { wr.println(checking[j].writeToString()); } } int
	 * numOfSavings = accInfo.getNumberOfSavingsAccounts();
	 * wr.println(numOfSavings); if(numOfSavings!=0) { SavingsAccount[]
	 * savings=accInfo.getSavingsAccounts(); for(int j=0;j<numOfSavings;j++) {
	 * wr.println(savings[j].writeToString()); } } int numOfCD =
	 * accInfo.getNumberOfCDAccounts(); wr.println(numOfCD); if(numOfCD!=0) {
	 * CDAccount[] cd=accInfo.getCDAccounts(); for(int j=0;j<numOfSavings;j++) {
	 * wr.println(cd[j].writeToString()); } } } wr.close(); return true; } catch
	 * (IOException e) { return false; } }
	 */
	
	@Override
	public List<AccountHolder> sortAccountHolders() {
		Collections.sort(accHolderList); 
		return accHolderList; 
	}

	public ArrayList<AccountHolder> accHolderList = new ArrayList<AccountHolder>();
	private long nextAccNumber = 1;
	private List<CDOffering> offerings;

	private List<CDOffering> offeringsList = new ArrayList<>();
	
	@Override
	public void addCDOffering(CDOffering cdo) {
		offeringsList.add(cdo);
	}
	
	@Override
	public List<CDOffering> getCDOffering() {
		return offeringsList;
	}

	@Override
	public void setCDOfferings(CDOffering offerings) {
		// TODO Auto-generated method stub
		
	}
}
