package io.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import io.bankapp.model.Accounts;
import io.bankapp.model.Logger;
import io.bankapp.service.AccountService;
import java.util.HashMap;

@RestController
public class AccountController<accountService> {
	HashMap<Integer, accountService> User;
	@Autowired
	private AccountService accountService;
	@Autowired
	private LoggerController loggerController;

	// createAccount happens upon createCustomer
	public void createAccount(int acctID, int balance, String acctStatus) {

		this.User = new HashMap<>();
		this.User.put(acctID, (accountService) new Accounts(acctID, balance, "Active"));
		//Accounts acct = new Accounts(acctID, balance, acctStatus);
		//accountService.createAccount(acct);
	}

	// checkBalance
	@GetMapping("/account/{acctID}/balance")
	public int getBalance(@PathVariable int acctID) {
		if( User.containsKey(acctID)) {
			return accountService.getBalance(acctID);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");
		return 0;
	}

	// depositAmount
	@PutMapping("/account/{acctID}/deposit/{amount}")
	public void depositAmount(@PathVariable int acctID, @PathVariable int amount) {
		if( User.containsKey(acctID)) {
			int initBal = getBalance(acctID);
			accountService.depositAmount(acctID, amount);
			Logger logger = new Logger(acctID, "Deposited", "Success", initBal, initBal + amount);
			loggerController.addLog(logger);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");

	}

	// withdrawAmount
	@PutMapping("/account/{acctID}/withdraw/{amount}")
	public void withdrawAmount(@PathVariable int acctID, @PathVariable int amount) {
		if( User.containsKey(acctID)) {
			int initBal = getBalance(acctID);
			accountService.withdrawAmount(acctID, amount);
			Logger logger = new Logger(acctID, "Withdrawn", "Success", initBal, initBal - amount);
			loggerController.addLog(logger);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");
	}

	// transferAmount
	@PutMapping("/account/{acctID}/transfer/{destAcctID}/{amount}")
	public void transferAmount(@PathVariable int acctID, @PathVariable int destAcctID, @PathVariable int amount) {
		if( User.containsKey(acctID)) {
			int initBalSender = getBalance(acctID);
			int initBalReceiver = getBalance(destAcctID);
			accountService.transferAmount(acctID, destAcctID, amount);
			Logger loggerSender = new Logger(acctID, "Transferred", "Success", initBalSender, initBalSender - amount);
			loggerController.addLog(loggerSender);
			Logger loggerReceiver = new Logger(destAcctID, "Received", "Success", initBalReceiver,
					initBalReceiver + amount);
			loggerController.addLog(loggerReceiver);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");
	}

	// deleteAccount
	@DeleteMapping("/account/{acctID}")
	public void deleteAccount(@PathVariable int acctID) {
		if( User.containsKey(acctID)) {
			accountService.deleteAccount(acctID);
			loggerController.deleteLog(acctID);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");
	}

	// getAccountInfo
	@GetMapping("/account/{acctID}")
	public Accounts getAccountInfo(@PathVariable int acctID) {
		if( User.containsKey(acctID)) {
			return accountService.getAccountInfo(acctID);
		}
		else
			System.out.println( "User: " + acctID+ "doesn't exist!");
		return null;
	}

}