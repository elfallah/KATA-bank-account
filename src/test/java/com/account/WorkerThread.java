package com.account;

import java.math.BigDecimal;

import com.account.model.Account;
import com.account.model.Amount;
import com.account.model.Balance;
import com.account.model.Transaction;

public class WorkerThread implements Runnable {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WorkerThread.class);

	private BigDecimal value;
	private Account account;

	public WorkerThread(BigDecimal value, Account account) {
		this.value = value;
		this.account = account;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		log.info(Thread.currentThread().getName() + " Start.");
		processCommand();
		log.info(Thread.currentThread().getName() + " End.");
	}

	private void processCommand() {
		try {
			account.withdraw(new Amount(value));
			Transaction.TransactionBuilder.getInstance().execute(account);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
